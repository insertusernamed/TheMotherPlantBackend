package org.insertusernamed.themotherplant.plant;

import org.insertusernamed.themotherplant.exception.ResourceNotFoundException;
import org.insertusernamed.themotherplant.external.CloudflareR2Client;
import org.insertusernamed.themotherplant.plant.dto.CreatePlantRequest;
import org.insertusernamed.themotherplant.plant.dto.PlantResponse;
import org.insertusernamed.themotherplant.tag.Tag;
import org.insertusernamed.themotherplant.tag.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlantService {

	private final PlantRepository plantRepository;
	private final TagRepository tagRepository;
	private final CloudflareR2Client cloudflareR2Client;

	public PlantService(PlantRepository plantRepository, TagRepository tagRepository, CloudflareR2Client cloudflareR2Client) {
		this.cloudflareR2Client = cloudflareR2Client;
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

	public List<Plant> createPlants(List<CreatePlantRequest> plantRequests, List<MultipartFile> files) throws IOException {
		if (plantRequests.size() != files.size()) {
			throw new IllegalArgumentException("The number of plant data objects must match the number of files.");
		}

		List<Plant> savedPlants = new ArrayList<>();

		for (int i = 0; i < plantRequests.size(); i++) {
			CreatePlantRequest request = plantRequests.get(i);
			MultipartFile file = files.get(i);

			String imageUrl = cloudflareR2Client.uploadFile(file);

			List<Tag> tags = tagRepository.findByNameIn(request.tags());
			Set<Tag> tagsAsSet = new HashSet<>(tags);

			Plant newPlant = new Plant();
			newPlant.setCommonName(request.commonName());
			newPlant.setDescription(request.description());
			newPlant.setPrice(request.price());
			newPlant.setImageUrl(imageUrl);
			newPlant.setTags(tagsAsSet);

			savedPlants.add(plantRepository.save(newPlant));
		}

		return savedPlants;
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
