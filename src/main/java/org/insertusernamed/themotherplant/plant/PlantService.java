package org.insertusernamed.themotherplant.plant;

import org.insertusernamed.themotherplant.exception.ResourceNotFoundException;
import org.insertusernamed.themotherplant.plant.dto.PlantResponse;
import org.insertusernamed.themotherplant.tag.Tag;
import org.insertusernamed.themotherplant.tag.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantService {

	private final PlantRepository plantRepository;
	private final TagRepository tagRepository;

	public PlantService(PlantRepository plantRepository, TagRepository tagRepository) {
		this.plantRepository = plantRepository;
		this.tagRepository = tagRepository;
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

	public PlantResponse createPlant(Plant plant) {
		Plant savedPlant = plantRepository.save(plant);
		return convertToResponse(savedPlant);
	}

	public void deletePlant(Long id) {
		if (!plantRepository.existsById(id)) {
			throw new ResourceNotFoundException("Plant not found with id: " + id);
		}
		plantRepository.deleteById(id);
		System.out.println("Plant with id " + id + " deleted successfully.");
	}

	public PlantResponse addTagToPlant(Long plantId, Long tagId) {
		Plant plant = plantRepository.findById(plantId)
				.orElseThrow(() -> new ResourceNotFoundException("Plant not found with id: " + plantId));
		Tag tag = tagRepository.findById(tagId)
				.orElseThrow(() -> new ResourceNotFoundException("Tag not found with id: " + tagId));

		if (plant.getTags().stream().anyMatch(t -> t.getId().equals(tagId))) {
			throw new IllegalArgumentException("Tag with id " + tagId + " is already associated with plant with id " + plantId);
		}

		plant.getTags().add(tag);
		Plant savedPlant = plantRepository.save(plant);
		return convertToResponse(savedPlant);
	}

	public PlantResponse removeTagFromPlant(Long plantId, Long tagId) {
		Plant plant = plantRepository.findById(plantId)
				.orElseThrow(() -> new ResourceNotFoundException("Plant not found with id: " + plantId));

		if (plant.getTags().stream().noneMatch(t -> t.getId().equals(tagId))) {
			throw new ResourceNotFoundException("Tag with id " + tagId + " is not associated with plant with id " + plantId);
		}

		plant.getTags().removeIf(tag -> tag.getId().equals(tagId));
		Plant savedPlant = plantRepository.save(plant);
		return convertToResponse(savedPlant);
	}

	private PlantResponse convertToResponse(Plant plant) {
		return new PlantResponse(
				plant.getId(),
				plant.getCommonName(),
				plant.getDescription(),
				plant.getImageUrl(),
				plant.getPrice(),
				plant.getTags()
		);
	}
}
