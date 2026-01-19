package com.next.engine.data;

import com.next.engine.dto.Frame;
import com.next.engine.dto.Point;
import com.next.engine.dto.Rectangle;
import com.next.engine.dto.TextureMetadata;
import com.next.engine.graphics.Sprite;

import java.awt.image.BufferedImage;
import java.util.List;

public final class AtlasImporter {

    public static void register(BufferedImage sheet, TextureMetadata metadata) {
        if (sheet == null || metadata == null) {
            throw new IllegalArgumentException("Sheet or metadata cannot be null");
        }

        Registry.textures.put(99, sheet);
        Registry.textureIds.put("master_sheet", 99);    // Never actually queried, just here for safety

        List<Frame> frames = metadata.getFrames();
        Registry.sprites = new Sprite[frames.size()];

        for (int i = 0; i < frames.size(); i++) {
            Frame frame = frames.get(i);
            String filename = frame.getFilename();
            Rectangle coordinates = frame.getFrame();
            Point pivot = frame.getPivot();
            float pivotX = 0, pivotY = 0;
            if (pivot != null) {
                pivotX = coordinates.width * frame.getPivot().x;
                pivotY = coordinates.height * frame.getPivot().y;
            }

            Sprite sprite = new Sprite(
                    i, filename,
                    coordinates.x, coordinates.y, coordinates.width, coordinates.height,
                    pivotX, pivotY
            );

            Registry.textureIds.put(filename, i);
            Registry.sprites[i] = sprite;
        }
    }
}
