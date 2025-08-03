package org.insertusernamed.themotherplant.external;

import lombok.RequiredArgsConstructor;
import org.insertusernamed.themotherplant.external.dto.PlantNetResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/identify")
@RequiredArgsConstructor
public class IdentificationController {

	private final IdentificationService identificationService;

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<PlantNetResponse> identifyPlant(
			@RequestPart("file") MultipartFile file,
			@RequestParam(value = "organ", defaultValue = "leaf") String organ) throws IOException {
		PlantNetResponse response = identificationService.identifyPlantFromImage(file, organ);
		return ResponseEntity.ok(response);
	}
}
