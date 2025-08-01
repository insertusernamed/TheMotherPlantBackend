package org.insertusernamed.themotherplant.plant.dto;

import org.insertusernamed.themotherplant.plant.Tag;

import java.math.BigDecimal;
import java.util.Set;

public record PlantResponse (
	Long id,
	String scientificName,
	String commonName,
	String description,
	String imageUrl,
	BigDecimal price,
	Set<Tag> tags
) {}