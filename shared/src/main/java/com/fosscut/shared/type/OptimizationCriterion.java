package com.fosscut.shared.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OptimizationCriterion {
    @JsonProperty("MinCost")
    MIN_COST,
    @JsonProperty("MinWaste")
    MIN_WASTE,
    @JsonProperty("MinWasteExperimental")
    MIN_WASTE_EXPERIMENTAL
}
