package com.next.engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Size {
    @JsonProperty("w")
    public int width;
    @JsonProperty("h")
    public int height;
}
