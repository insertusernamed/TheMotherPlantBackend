package org.insertusernamed.themotherplant.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiStructuredResponse(
		List<String> descriptions,
		PriceSuggestions priceSuggestions,
		List<String> tags
) {
}

@JsonIgnoreProperties(ignoreUnknown = true)
record PriceSuggestions(
		BigDecimal low,
		BigDecimal mid,
		BigDecimal high
) {
}