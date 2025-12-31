package com.next.engine.data;

import com.next.engine.annotations.internal.Experimental;

@Experimental
public class RenderBuffer {

    public void alloc() {
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect(1024);
    }
}
