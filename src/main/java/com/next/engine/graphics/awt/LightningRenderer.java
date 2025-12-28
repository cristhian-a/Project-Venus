package com.next.engine.graphics.awt;

import com.next.engine.Global;
import com.next.engine.model.Camera;
import com.next.engine.util.Experimental;
import com.next.system.AssetRegistry;
import com.next.system.Settings;

import java.awt.*;
import java.awt.image.BufferedImage;

@Experimental
class LightningRenderer {

    private AssetRegistry assets;
    private Settings.VideoSettings settings;

    private final BufferedImage lightMap;
    private final Graphics2D lightGraphics;
    private float ambient = 0.5f;

    public LightningRenderer(AssetRegistry assets, Settings.VideoSettings settings) {
        this.assets = assets;
        this.settings = settings;

        lightMap = new BufferedImage(
                settings.WIDTH / settings.SCALE,
                settings.HEIGHT / settings.SCALE,
                BufferedImage.TYPE_INT_ARGB
        );

        lightGraphics = lightMap.createGraphics();
        // Remove this if a raw light texture is preferred (they look sick)
        lightGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    }

    @Experimental
    protected void punchLightMap(Camera camera) {
        lightGraphics.setComposite(AlphaComposite.Src);
        lightGraphics.setColor(new Color(0, 0, 0, (int) (ambient * 255)));
        lightGraphics.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());

        // AlphaComposite.getInstance(AlphaComposite.DST_OUT, 0.8f)
        // should be used (instead of just getting AlphaComposite.DstOut) to prevent light bleeding in case
        // many "light holes" are punched together, so we preserve some darkness.
        lightGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 0.8f));

        // sign-wave calculation to get the flicker effect's size
        double t = Global.getTime() * 3d;
        float pulse = (float) (Math.sin(t) * 0.5f + 0.5f);

        float base = 16f;
        float size = base + pulse * 6f;

        float worldX = 376f;
        float worldY = 344f;

        float lightScale = 8;
        float drawW = size * lightScale;
        float drawH = size * lightScale;

        float drawX = worldX - (drawW / 2);
        float drawY = worldY - (drawH / 2);

        BufferedImage light = assets.getTextureSheet("light").getSprite(3);
        lightGraphics.drawImage(
                light,
                camera.worldToScreenX(drawX),
                camera.worldToScreenY(drawY),
                (int) (size * lightScale), (int) (size * lightScale),
                null
        );
    }

    public void render(Graphics2D g, Camera camera) {
        // Experimental stuff
        punchLightMap(camera);

        g.setComposite(AlphaComposite.SrcOver);
        g.drawImage(lightMap, 0, 0, null);
    }

}
