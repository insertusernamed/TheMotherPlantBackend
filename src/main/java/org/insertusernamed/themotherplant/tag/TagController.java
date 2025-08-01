package org.insertusernamed.themotherplant.tag;

import org.insertusernamed.themotherplant.tag.dto.TagResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tags")
public class TagController {

	private final TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	@GetMapping
	public ResponseEntity<List<TagResponse>> getAllTags() {
		List<TagResponse> tags = tagService.getAllTags();
		return ResponseEntity.ok(tags);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TagResponse> getTagById(@PathVariable Long id) {
		TagResponse tag = tagService.getTagById(id);
		return ResponseEntity.ok(tag);
	}

	@PostMapping
	public ResponseEntity<TagResponse> createTag(@RequestBody Tag tag) {
		TagResponse createdTag = tagService.createTag(tag);
		return ResponseEntity.status(201).body(createdTag);
	}

	@DeleteMapping("/{id}")
	public void deleteTag(@PathVariable Long id) {
		tagService.deleteTag(id);
	}
}
