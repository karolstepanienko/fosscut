package com.fosscut.api.type;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Algorithm {
    FFD,
    @JsonProperty("Greedy")
    GREEDY,
    CG
}
