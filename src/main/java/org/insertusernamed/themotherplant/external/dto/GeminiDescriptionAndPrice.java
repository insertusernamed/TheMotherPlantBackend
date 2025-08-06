package org.insertusernamed.themotherplant.external.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GeminiDescriptionAndPrice(
		List<String> descriptions,
		Map<String, BigDecimal> priceSuggestions
) {}