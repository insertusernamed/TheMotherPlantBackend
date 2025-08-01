package org.insertusernamed.themotherplant.plant;

import org.insertusernamed.themotherplant.exception.ResourceNotFoundException;
import org.insertusernamed.themotherplant.plant.dto.PlantResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

	private final PlantRepository plantRepository;

	public PlantService(PlantRepository plantRepository) {
		this.plantRepository = plantRepository;
	}

	public List<PlantResponse> findAllPlants() {
		return plantRepository.findAll()
				.stream()
				.map(this::convertToResponse)
				.collect(Collectors.toList());
	}

	public PlantResponse findById(Long id) {
		Plant plant = plantRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Plant not found with id: " + id));
		return convertToResponse(plant);
	}

	private PlantResponse convertToResponse(Plant plant) {
		return new PlantResponse(
				plant.getId(),
				plant.getScientificName(),
				plant.getCommonName(),
				plant.getDescription(),
				plant.getImageUrl(),
				plant.getPrice(),
				plant.getTags()
		);
	}
}
