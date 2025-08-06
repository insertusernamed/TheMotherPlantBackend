package org.insertusernamed.themotherplant.external;

import lombok.RequiredArgsConstructor;
import org.insertusernamed.themotherplant.external.dto.GeminiDescriptionAndPrice;
import org.insertusernamed.themotherplant.external.dto.PlantNetResponse;
import org.insertusernamed.themotherplant.external.dto.PlantNetUploadResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class IdentificationService {

	private final PlantNetClient plantNetClient;
	private final GeminiClient geminiClient;

	public PlantNetResponse identifyPlantFromImage(MultipartFile file, String organ) throws IOException {
		PlantNetUploadResponse uploadResponse = plantNetClient.uploadImage(file);
		String imageId = uploadResponse.id();

		if (imageId == null || imageId.isBlank()) {
			throw new RuntimeException("Failed to get a valid image ID from identifier");
		}

		return plantNetClient.identifyPlant(imageId, organ);
	}

	public GeminiDescriptionAndPrice getGeminiPlantDescription(String commonName) throws IOException {
		return geminiClient.generateDescriptionTextOnly(commonName);
	}
}
