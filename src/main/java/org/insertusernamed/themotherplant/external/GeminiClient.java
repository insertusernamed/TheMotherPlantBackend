package org.insertusernamed.themotherplant.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.insertusernamed.themotherplant.external.dto.GeminiStructuredResponse;
import org.insertusernamed.themotherplant.tag.TagService;
import org.insertusernamed.themotherplant.tag.dto.TagResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GeminiClient {

	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;
	private final TagService tagService;
	private final String apiKey;
	private final String promptTemplate;
	private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash-exp:generateContent";

	public GeminiClient(
			ObjectMapper objectMapper,
			RestTemplate restTemplate,
			TagService tagService,
			@Value("${google.gemini.api.key}") String apiKey,
			@Value("${google.gemini.api.prompt.template}") String promptTemplate
	) {
		this.objectMapper = objectMapper;
		this.restTemplate = restTemplate;
		this.tagService = tagService;
		this.apiKey = apiKey;
		this.promptTemplate = promptTemplate;
	}

	@RateLimiter(name = "gemini-api-daily")
	public GeminiStructuredResponse generateDescriptionTextOnly(String plantName) throws IOException {
		String tagListString = tagService.getAllTags().stream()
				.map(TagResponse::name)
				.collect(Collectors.joining(", "));
		String prompt = String.format(promptTemplate, plantName, tagListString);

		String requestBody = buildTextOnlyRequestBody(prompt);
		return getGeminiDescriptionAndPrice(requestBody);
	}

	private GeminiStructuredResponse getGeminiDescriptionAndPrice(String requestBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		try {
			String url = GEMINI_API_URL + "?key=" + apiKey;
			ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

			if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
				return parseResponse(response.getBody());
			} else {
				throw new RuntimeException("Failed to get response from Gemini API: " + response.getStatusCode());
			}

		} catch (Exception e) {
			System.err.println("Error calling Gemini API: " + e.getMessage());
			throw new RuntimeException("Failed to call Gemini API", e);
		}
	}

	// This method i wont delete yet because i might use it later
	public GeminiStructuredResponse generateDescriptionWithImage(String plantName, MultipartFile image) throws IOException {
		String prompt = String.format(promptTemplate, plantName);

		String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
		String requestBody = buildMultimodalRequestBody(prompt, base64Image, image.getContentType());
		return getGeminiDescriptionAndPrice(requestBody);
	}

	private String buildTextOnlyRequestBody(String prompt) throws JsonProcessingException {
		Map<String, Object> requestPayload = Map.of(
				"contents", List.of(
						Map.of(
								"parts", List.of(
										Map.of("text", prompt)
								)
						)
				),
				"tools", List.of(
						Map.of("google_search", Map.of())
				),
				"generationConfig", Map.of(
						"temperature", 0.7,
						"maxOutputTokens", 2048
				)
		);

		return objectMapper.writeValueAsString(requestPayload);
	}

	private String buildMultimodalRequestBody(String prompt, String base64Image, String mimeType) throws JsonProcessingException {
		Map<String, Object> requestPayload = Map.of(
				"contents", List.of(
						Map.of(
								"parts", List.of(
										Map.of("text", prompt),
										Map.of(
												"inline_data", Map.of(
														"mime_type", mimeType,
														"data", base64Image
												)
										)
								)
						)
				),
				// grounding isnt available for multimodal requests
				"generationConfig", Map.of(
						"temperature", 0.7,
						"maxOutputTokens", 2048
				)
		);

		return objectMapper.writeValueAsString(requestPayload);
	}

	private GeminiStructuredResponse parseResponse(String responseBody) throws JsonProcessingException {
		JsonNode rootNode = objectMapper.readTree(responseBody);

		JsonNode candidatesNode = rootNode.get("candidates");
		if (candidatesNode == null || candidatesNode.isEmpty()) {
			throw new RuntimeException("No candidates found in Gemini response");
		}

		JsonNode contentNode = candidatesNode.get(0).get("content");
		if (contentNode == null) {
			throw new RuntimeException("No content found in Gemini response");
		}

		JsonNode partsNode = contentNode.get("parts");
		if (partsNode == null || partsNode.isEmpty()) {
			throw new RuntimeException("No parts found in Gemini response");
		}

		String textResponse = partsNode.get(0).get("text").asText();

		try {
			// cleaning up the response in case it includes markdown code fences
			String cleanedJson = textResponse
					.replace("```json", "")
					.replace("```", "")
					.trim();

			return objectMapper.readValue(cleanedJson, GeminiStructuredResponse.class);
		} catch (JsonProcessingException e) {
			System.err.println("Failed to parse JSON from Gemini: " + textResponse);
			throw new RuntimeException("Failed to parse response from Gemini.", e);
		}
	}
}