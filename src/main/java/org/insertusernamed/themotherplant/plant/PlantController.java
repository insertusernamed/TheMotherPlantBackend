package org.insertusernamed.themotherplant.plant;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.insertusernamed.themotherplant.plant.dto.CreatePlantRequest;
import org.insertusernamed.themotherplant.plant.dto.PlantResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plants")
public class PlantController {

	private final PlantService plantService;
	private final ObjectMapper objectMapper;

	public PlantController(PlantService plantService, ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		this.plantService = plantService;
	}

	@GetMapping
	public ResponseEntity<List<PlantResponse>> getAllPlants() {
		List<PlantResponse> plants = plantService.findAllPlants();
		return ResponseEntity.ok(plants);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PlantResponse> getPlantById(@PathVariable Long id) {
		PlantResponse plant = plantService.findById(id);
		return ResponseEntity.ok(plant);
	}

    @GetMapping("/latest/{count}")
    public ResponseEntity<List<PlantResponse>> getLatestPlants(@PathVariable int count) {
        List<PlantResponse> plants = plantService.getLatestPlants(count);
        return ResponseEntity.ok(plants);
    }

	@PostMapping
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<Plant>> createPlants(
			@RequestPart("plantsJson") String plantsJson,
			@RequestPart("files") List<MultipartFile> files
	) throws Exception {

		TypeReference<List<CreatePlantRequest>> typeRef = new TypeReference<>() {
		};
		List<CreatePlantRequest> plantRequests = objectMapper.readValue(plantsJson, typeRef);

		List<Plant> createdPlants = plantService.createPlants(plantRequests, files);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdPlants);
	}

	@DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
		plantService.deletePlant(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{plantId}/tags/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
	public PlantResponse addTagToPlant(@PathVariable Long plantId, @PathVariable Long tagId) {
		return plantService.addTagToPlant(plantId, tagId);
	}

	@DeleteMapping("/{plantId}/tags/{tagId}")
    @PreAuthorize("hasRole('ADMIN')")
	public PlantResponse removeTagFromPlant(@PathVariable Long plantId, @PathVariable Long tagId) {
		return plantService.removeTagFromPlant(plantId, tagId);
	}
}
