package com.next.model;

public interface Equip {
    default int getMight() { return 0; }
    default int getResistance() { return 0; }
}
