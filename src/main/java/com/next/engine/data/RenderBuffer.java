package com.next.engine.data;

public class RenderBuffer {

    public void alloc() {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect(1024);
    }
}
