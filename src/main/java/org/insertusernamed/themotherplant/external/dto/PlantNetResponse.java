package org.insertusernamed.themotherplant.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlantNetResponse(
		List<PlantNetResult> results
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record PlantNetResult(
		Double score,
		Species species
) {}

@JsonIgnoreProperties(ignoreUnknown = true)
record Species(
		String scientificName,
		List<String> commonNames,
		String family
) {}
