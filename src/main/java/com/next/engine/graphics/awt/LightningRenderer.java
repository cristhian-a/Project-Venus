package com.next.engine.graphics.awt;

import com.next.engine.Global;
import com.next.engine.data.Registry;
import com.next.engine.graphics.RenderQueue;
import com.next.engine.model.Camera;
import com.next.system.Settings.VideoSettings;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

class LightningRenderer {
    private final VideoSettings settings;
    private final BufferedImage lightMap;
    private final Graphics2D lightGraphics;
    private final AffineTransform identity = new AffineTransform();
    private final AlphaComposite[] compositeCache;

    private final int COMPOSITE_BUCKETS = 16;

    private float ambient = 0.8f;

    public LightningRenderer(VideoSettings settings) {
        this.settings = settings;

        lightMap = new BufferedImage(
                settings.WIDTH / settings.SCALE,
                settings.HEIGHT / settings.SCALE,
                BufferedImage.TYPE_INT_ARGB
        );

        lightGraphics = lightMap.createGraphics();
        lightGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        compositeCache = new AlphaComposite[COMPOSITE_BUCKETS + 1];
        for (int i = 0; i <= COMPOSITE_BUCKETS; i++) {
            float alpha = (float) i / COMPOSITE_BUCKETS;
            compositeCache[i] = AlphaComposite.getInstance(AlphaComposite.DST_OUT, alpha);
        }
    }

    private int alphaToBucket(float alpha) {
        int bucket = Math.round(alpha * COMPOSITE_BUCKETS);
        if (bucket < 0) bucket = 0;
        if (bucket > COMPOSITE_BUCKETS) bucket = COMPOSITE_BUCKETS;
        return bucket;
    }

    private void punchLightMap(Camera camera, RenderQueue.LightTable lights) {
        lightGraphics.setTransform(identity);
        lightGraphics.setClip(null);

        lightGraphics.setComposite(AlphaComposite.Src);
        lightGraphics.setColor(new Color(0, 0, 0, (int) (ambient * 255)));
        lightGraphics.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());

        if (lights.count == 0) return;

        // sine-wave calculation to get the flicker effect's size
        double t = Global.getTime() * 3d;
        float pulse = (float) (Math.sin(t) * 0.5f + 0.5f);

        float strength = 0.2f;
        float flicker = 1f + pulse * strength;

        for (int i = 0; i < lights.count; i++) {
            float x = lights.x[i];
            float y = lights.y[i];
            float radius = lights.radius[i];
            float intensity = lights.intensity[i];

            float finalRadius = flicker * (radius * settings.SCALE);
            float drawX = x - (finalRadius / 2);
            float drawY = y - (finalRadius / 2);

            float alpha = Math.clamp(intensity * Global.alphaBaseFactor, 0, 1);
            int bucket = alphaToBucket(alpha);
            lightGraphics.setComposite(compositeCache[bucket]);

            BufferedImage light = Registry.textures.get(lights.textureIds[i]);
            lightGraphics.drawImage(
                    light,
                    camera.worldToScreenX(drawX),
                    camera.worldToScreenY(drawY),
                    (int) finalRadius, (int) finalRadius,
                    null
            );
        }
    }

    public void render(Graphics2D g, Camera camera, RenderQueue.LayerBucket bucket) {
        punchLightMap(camera, bucket.lights);

        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(lightMap, 0, 0, null);
    }

}
