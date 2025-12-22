package com.next.engine.sound;

import com.next.engine.event.GameEvent;

public sealed interface AudioCommand extends GameEvent permits PlaySound, StopSound, SetVolume  {
}
