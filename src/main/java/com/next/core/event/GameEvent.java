package com.next.core.event;

import com.next.model.Player;

public sealed interface GameEvent {

    record KeyPickedUp() implements GameEvent {}
    record SpellPickedUp(Player player) implements GameEvent {}
    record DoorOpened(Player player) implements GameEvent {}
}
