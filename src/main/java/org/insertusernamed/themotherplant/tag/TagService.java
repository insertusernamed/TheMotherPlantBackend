package org.insertusernamed.themotherplant.tag;

import org.insertusernamed.themotherplant.exception.ResourceNotFoundException;
import org.insertusernamed.themotherplant.tag.dto.TagResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

	private final TagRepository tagRepository;

	public TagService(TagRepository tagRepository) {
		this.tagRepository = tagRepository;
	}

	public List<TagResponse> getAllTags() {
		return tagRepository.findAll()
				.stream()
				.map(this::convertToResponse)
				.collect(Collectors.toList());
	}

	public TagResponse createTag(Tag tag) {
		if (tag.getName() == null || tag.getName().isEmpty()) {
			throw new IllegalArgumentException("Tag name cannot be null or empty");
		}

		Tag savedTag = tagRepository.save(tag);
		return convertToResponse(savedTag);
	}

	public TagResponse getTagById(Long id) {
		Tag tag = tagRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No tag found with id: " + id));
		return convertToResponse(tag);
	}

	public void deleteTag(Long id) {
		if (!tagRepository.existsById(id)) {
			throw new ResourceNotFoundException("No tag found with id: " + id);
		}
		tagRepository.deleteById(id);
		System.out.println("Tag with id " + id + " deleted successfully.");
	}

	private TagResponse convertToResponse(Tag tag) {
		return new TagResponse(
				tag.getId(),
				tag.getName(),
				tag.getDescription()
		);
	}
}
