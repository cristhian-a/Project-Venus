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
    private final Color[] ambientCache;

    private final int COMPOSITE_BUCKETS = 16;
    private final int AMBIENT_BUCKETS = 16;

    private float ambient = 0.8f;

    private BufferedImage cachedColoredLight;

    public LightningRenderer(VideoSettings settings) {
        this.settings = settings;

        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();

        lightMap = gc.createCompatibleImage(
                settings.WIDTH / settings.SCALE,
                settings.HEIGHT / settings.SCALE,
                Transparency.TRANSLUCENT
        );

        lightGraphics = lightMap.createGraphics();
        lightGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        compositeCache = new AlphaComposite[COMPOSITE_BUCKETS + 1];
        for (int i = 0; i <= COMPOSITE_BUCKETS; i++) {
            float alpha = (float) i / COMPOSITE_BUCKETS;
            compositeCache[i] = AlphaComposite.getInstance(AlphaComposite.DST_OUT, alpha);
        }

        ambientCache = new Color[AMBIENT_BUCKETS + 1];
        for (int i = 0; i <= AMBIENT_BUCKETS; i++) {
            float alpha = (float) i / AMBIENT_BUCKETS;
            ambientCache[i] = new Color(0, 0, 0, (int) (alpha * 255));
        }
    }

    private int alphaToBucket(float alpha) {
        int bucket = Math.round(alpha * COMPOSITE_BUCKETS);
        if (bucket < 0) bucket = 0;
        if (bucket > COMPOSITE_BUCKETS) bucket = COMPOSITE_BUCKETS;
        return bucket;
    }

    private int ambientToBucket(float ambient) {
        int bucket = Math.round(ambient * AMBIENT_BUCKETS);
        if (bucket < 0) bucket = 0;
        if (bucket > AMBIENT_BUCKETS) bucket = AMBIENT_BUCKETS;
        return bucket;
    }

    private void punchLightMap(Camera camera, RenderQueue.LightTable lights) {
        lightGraphics.setTransform(identity);
        lightGraphics.setClip(null);

        lightGraphics.setComposite(AlphaComposite.Src);
        lightGraphics.setColor(ambientCache[ambientToBucket(ambient)]);
        lightGraphics.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());

        if (lights.count == 0) return;

        // sine-wave calculation to get the flicker effect's size
        double t = Global.getTime() * 3d;
        float pulse = (float) (Math.sin(t) * 0.5f + 0.5f);

        float strength = 0.25f;
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

            if (cachedColoredLight == null) {
                cachedColoredLight = makeColoredLight(light, new Color(0, 255, 0));
            }

            lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            lightGraphics.drawImage(
                    cachedColoredLight,
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

    private BufferedImage makeColoredLight(BufferedImage mask, Color color) {
        BufferedImage img = new BufferedImage(
                mask.getWidth(), mask.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < mask.getHeight(); y++) {
            for (int x = 0; x < mask.getWidth(); x++) {
//                int a = alphaRemap[(mask.getRGB(x, y) >> 24) & 0xFF];
                int a = (mask.getRGB(x, y) >> 24) & 0xFF;

                int aOut;
//                aOut = (int) compressAlphaByDiv(a, 2);
//                aOut = compressAlphaByClamp(a, 0, 155);

//                aOut = (int) (compressAlpha((a / 255f), 0.5f, 2f) * 255);
//                aOut = (int) (compressAlpha((a / 255f), 0f, 0.6f, 0.5f) * 255);
                aOut = (int) highCut(a, 0.4f, 1.3f);

                int r = color.getRed() * aOut / 255;
                int g = color.getGreen() * aOut / 255;
                int b = color.getBlue() * aOut / 255;

                int argb = (aOut << 24) | (r << 16) | (g << 8) | b;
                img.setRGB(x, y, argb);
            }
        }

        return img;
    }

    private float compressAlpha(float alpha, float minVal, float maxVal, float threshold) {
        if (alpha < threshold) {
            // Upward compression: Elevate the "valleys"
            // Normalizes 0..threshold to 0..1, then maps to minVal..threshold
            return minVal + (alpha / threshold) * (threshold - minVal);
        } else {
            // Downward compression: Cut the "peaks"
            // Normalizes threshold..1 to 0..1, then maps to threshold..maxVal
            return (float) (threshold + ((alpha - threshold) / (1.0 - threshold)) * (maxVal - threshold));
        }
    }

    private float compressAlpha(float alpha, float threshold, float ratio) {
        return threshold + (alpha - threshold) / ratio;
    }

    private float highCut(float alpha, float threshold, float ratio) {
        float t = alpha / 255f;

        float knee = 0.1f;

        float out;
        if (t < threshold - knee) {
            out = t;
        } else if (t > threshold + knee) {
            out = threshold + (t - threshold) / ratio;
        } else {
            float k = (t - (threshold - knee)) / (2 * knee);
            float soft = k * k * (3 - 2 * k);
            float linear = threshold + (t - threshold) / ratio;
            out = t * (1 - soft) + linear * soft;
        }

        // normalize so max still reaches 1.0
        float maxOut = threshold + (1.0f - threshold) / ratio;
        out /= maxOut;

        return (int)(out * 255);
    }

    private int compressAlphaByClamp(long alpha, int min, int max) {
        return Math.clamp(alpha, min, max);
    }

    private float compressAlphaByDiv(float alpha, float div) {
        return alpha / div;
    }

}
