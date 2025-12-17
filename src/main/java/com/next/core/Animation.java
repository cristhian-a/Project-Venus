package com.next.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Animation {
    public int[] frames;
    public int frameRate;   //  number of frames until transition
    public boolean loop;
}
