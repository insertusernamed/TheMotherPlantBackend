package org.insertusernamed.themotherplant.external;

import org.insertusernamed.themotherplant.external.dto.PlantNetResponse;
import org.insertusernamed.themotherplant.external.dto.PlantNetUploadResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
public class PlantNetClient {

	private final WebClient webClient;

	@Value("${plantnet.upload.url}")
	private String uploadUrl;

	@Value("${plantnet.identify.url}")
	private String identifyUrl;

	@Value("${plantnet.image.baseurl}")
	private String imageBaseUrl;

	public PlantNetClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.build();
	}

	public PlantNetUploadResponse uploadImage(MultipartFile file) throws IOException {
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new ByteArrayResource(file.getBytes()) {
			@Override
			public String getFilename() {
				return file.getOriginalFilename();
			}
		});

		return webClient.post()
				.uri(uploadUrl)
				.header(HttpHeaders.USER_AGENT, "Mozilla/5.0")
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.body(BodyInserters.fromMultipartData(body))
				.retrieve()
				.bodyToMono(PlantNetUploadResponse.class)
				.block();
	}

	public PlantNetResponse identifyPlant(String imageId, String organ) {
		Map<String, Object> imagePayload = Map.of(
				"url", imageBaseUrl + imageId,
				"organ", organ
		);
		Map<String, Object> requestPayload = Map.of("images", Collections.singletonList(imagePayload));

		return webClient.post()
				.uri(identifyUrl)
				.header(HttpHeaders.USER_AGENT, "Mozilla/5.0")
				.header(HttpHeaders.ORIGIN, "https://identify.plantnet.org")
				.header(HttpHeaders.REFERER, "https://identify.plantnet.org/")
				.contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromValue(requestPayload))
				.retrieve()
				.bodyToMono(PlantNetResponse.class)
				.block();
	}
}
