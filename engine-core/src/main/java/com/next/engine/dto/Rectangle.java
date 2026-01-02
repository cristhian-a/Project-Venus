package com.next.engine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Rectangle {
    public int x;
    public int y;
    @JsonProperty("w")
    public int width;
    @JsonProperty("h")
    public int height;
}
