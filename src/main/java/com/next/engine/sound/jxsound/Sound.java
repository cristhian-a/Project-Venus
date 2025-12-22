package com.next.engine.sound.jxsound;

import com.next.engine.sound.SoundData;

import javax.sound.sampled.*;

final class Sound {

    private Clip clip;
    private FloatControl gain;

    public void setTrack(SoundData data) throws LineUnavailableException {
        clip = AudioSystem.getClip();
        clip.open(data.format, data.pcmData, 0, data.pcmData.length);

        gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
    }

    public void play() {
        clip.start();
    }

    public void stop() {
        clip.stop();
        clip.close();
    }

    public void pause() {
        clip.stop();
    }

    public void restart() {
        clip.setFramePosition(0);
        play();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void setVolume(float volume) {
        gain.setValue(volume);
    }
}
