package org.insertusernamed.themotherplant.plant.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreatePlantRequest(
		String commonName,
		String description,
		BigDecimal price,
		List<String> tags
) {
}
