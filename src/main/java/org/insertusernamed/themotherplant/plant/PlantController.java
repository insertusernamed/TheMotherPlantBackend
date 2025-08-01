package org.insertusernamed.themotherplant.plant;

import org.insertusernamed.themotherplant.plant.dto.PlantResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
