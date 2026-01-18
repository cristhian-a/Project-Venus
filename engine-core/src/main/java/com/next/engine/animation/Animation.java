package com.next.engine.animation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animation {
    public int[] frames;
    public boolean loop;
    public double frameDuration;
}
