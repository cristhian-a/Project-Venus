package com.next.engine.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Frame {
    private String filename;
    private Rectangle frame;
    private boolean rotated;
    private boolean trimmed;
    private Rectangle spriteSourceSize;
    private Rectangle sourceSize;
    private Point pivot;
}
