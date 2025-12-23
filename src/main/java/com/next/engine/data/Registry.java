package com.next.engine.data;

import com.next.engine.sound.SoundClip;
import com.next.engine.sound.SoundData;

import java.util.HashMap;
import java.util.Map;

public final class Registry {
    public static final Map<SoundClip, SoundData> audioTracks = new HashMap<>();
}
