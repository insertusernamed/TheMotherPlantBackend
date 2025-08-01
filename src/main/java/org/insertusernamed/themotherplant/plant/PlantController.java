package org.insertusernamed.themotherplant.plant;

import org.insertusernamed.themotherplant.plant.dto.PlantResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plants")
public class PlantController {

	private final PlantService plantService;

	public PlantController(PlantService plantService) {
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

	@PostMapping
	public ResponseEntity<PlantResponse> createPlant(@RequestBody Plant plant) {
		PlantResponse createdPlant = plantService.createPlant(plant);
		return ResponseEntity.status(201).body(createdPlant);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePlant(@PathVariable Long id) {
		plantService.deletePlant(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{plantId}/tags/{tagId}")
	public PlantResponse addTagToPlant(@PathVariable Long plantId, @PathVariable Long tagId) {
	    return plantService.addTagToPlant(plantId, tagId);
	}

	@DeleteMapping("/{plantId}/tags/{tagId}")
	public PlantResponse removeTagFromPlant(@PathVariable Long plantId, @PathVariable Long tagId) {
	    return plantService.removeTagFromPlant(plantId, tagId);
	}
}
