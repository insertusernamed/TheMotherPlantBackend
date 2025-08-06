package org.insertusernamed.themotherplant.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.insertusernamed.themotherplant.external.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class GeminiClient {

	private final WebClient webClient;
	private final String apiKey;
	private final String geminiApiUrl;
	private final ObjectMapper objectMapper;
	@Value("${google.gemini.api.prompt.template}")
	private String promptTemplate;

	public GeminiClient(WebClient.Builder webClientBuilder, ObjectMapper objectMapper, @Value("${google.gemini.api.key}") String apiKey) {
		this.webClient = webClientBuilder.build();
		this.objectMapper = objectMapper;
		this.apiKey = apiKey;
		this.geminiApiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";
	}

	@RateLimiter(name = "gemini-api", fallbackMethod = "descriptionFallback")
	public GeminiDescriptionAndPrice generateDescription(String plantName, MultipartFile image) throws IOException {
		String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
		String prompt = String.format(promptTemplate, plantName);

		Part textPart = Part.fromText(prompt);
		Part imagePart = Part.fromImage(image.getContentType(), base64Image);
		Content content = new Content(List.of(textPart, imagePart));
		GeminiRequest request = new GeminiRequest(List.of(content));

		GeminiResponse response = webClient.post()
				.uri(geminiApiUrl + "?key=" + apiKey)
				.body(Mono.just(request), GeminiRequest.class)
				.retrieve()
				.bodyToMono(GeminiResponse.class)
				.block();

		if (response != null && !response.candidates().isEmpty()) {
			String jsonResponseText = response.candidates().getFirst().content().parts().getFirst().text();
			try {
				// cleaning up the response in case it includes markdown code fences
				String cleanedJson = jsonResponseText
						.replace("```json", "")
						.replace("```", "")
						.trim();
				return objectMapper.readValue(cleanedJson, GeminiDescriptionAndPrice.class);
			} catch (JsonProcessingException e) {
				System.err.println("Failed to parse JSON from Gemini: " + jsonResponseText);
				throw new RuntimeException("Failed to parse response from Gemini.", e);
			}
		}
		throw new RuntimeException("Received no valid candidates from Gemini.");	}

	public GeminiDescriptionAndPrice descriptionFallback(String plantName, MultipartFile image, Throwable t) {
	    System.err.println("Gemini rate limit exceeded! Falling back. Error: " + t.getMessage());
	    return new GeminiDescriptionAndPrice(
	        List.of(String.format(
	            "This beautiful %s is a wonderful addition to any home. (Description generation is currently busy, please try again shortly.)",
	            plantName
	        )),
	        Map.of("low", BigDecimal.valueOf(9.99), "mid", BigDecimal.valueOf(14.99), "high", BigDecimal.valueOf(19.99))
	    );
	}
}
