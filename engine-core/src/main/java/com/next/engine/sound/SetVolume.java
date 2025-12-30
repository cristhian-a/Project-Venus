package com.next.engine.sound;

public record SetVolume(float volume, SoundChannel channel) implements AudioCommand {
}
