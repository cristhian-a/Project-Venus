package com.next.engine.sound.jxsound;

import com.next.engine.sound.AudioBackend;
import com.next.engine.sound.SoundChannel;
import com.next.engine.sound.SoundClip;
import com.next.engine.sound.SoundData;

import javax.sound.sampled.LineUnavailableException;
import java.util.*;

public class JavaSoundBackend implements AudioBackend {

    private final Map<SoundClip, SoundData> tracks;
    private final Map<SoundClip, List<Sound>> playing = new HashMap<>();
    private final Map<SoundChannel, Float> volumes = new EnumMap<>(SoundChannel.class);
    private final Map<SoundChannel, List<SoundClip>> tracksByChannel = new EnumMap<>(SoundChannel.class);

    public JavaSoundBackend(Map<SoundClip, SoundData> tracks) {
        this.tracks = tracks;

        volumes.put(SoundChannel.MUSIC, -5f);
        volumes.put(SoundChannel.SFX, -5f);
    }

    @Override
    public void play(SoundClip clip, SoundChannel channel, boolean loop) {
        SoundData track = tracks.get(clip);
        Sound s = new Sound();
        try {
            s.setTrack(track);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        s.play();
        s.setVolume(volumes.get(channel));
        if (loop) {
            playing.computeIfAbsent(clip, _ -> new ArrayList<>()).add(s);
            tracksByChannel.computeIfAbsent(channel, _ -> new ArrayList<>()).add(clip);
            s.loop();
        }
    }

    @Override
    public void stop(SoundClip clip) {
        playing.getOrDefault(clip, Collections.emptyList()).forEach(Sound::stop);

        for (var soundList : tracksByChannel.values()) {
            soundList.remove(clip);
        }
    }

    @Override
    public void setVolume(float volume, SoundChannel channel) {
        volumes.put(channel, volume);
        List<SoundClip> clips = tracksByChannel.get(channel);
        if (clips != null) {
            for (var id : clips) {
                playing.get(id).forEach(s -> s.setVolume(volume));
            }
        }
    }
}
