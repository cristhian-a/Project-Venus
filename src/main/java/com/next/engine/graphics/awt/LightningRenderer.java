package com.next.engine.graphics.awt;

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
    private float ambient = 0.5f;

    int frame = 0;
    boolean growing = true;

    public LightningRenderer(AssetRegistry assets, Settings.VideoSettings settings) {
        this.assets = assets;
        this.settings = settings;

        lightMap = new BufferedImage(
                settings.WIDTH / settings.SCALE,
                settings.HEIGHT / settings.SCALE,
                BufferedImage.TYPE_INT_ARGB
        );
    }

    @Experimental
    protected void punchLightMap(Camera camera) {
        Graphics2D lg = lightMap.createGraphics();

        lg.setComposite(AlphaComposite.Src);
        lg.setColor(new Color(0, 0, 0, (int) (ambient * 255)));
        lg.fillRect(0, 0, lightMap.getWidth(), lightMap.getHeight());

        lg.setComposite(AlphaComposite.DstOut);
        float width = 16f;
        float height = 16f;

        if (frame < 60 && growing) {
            width += (float) (0.2 * frame);
            height += (float) (0.2 * frame);

            frame++;
            if (frame == 60) growing = false;
        } else if (!growing && frame > 1) {
            width += (float) (0.2 * frame);
            height += (float) (0.2 * frame);
            frame--;
        } else {
            frame = 0;
            growing = true;
        }

        float worldX = 376f;
        float worldY = 344f;
        float centerX = worldX - (width * 2);
        float centerY = worldY - (height * 2);

        BufferedImage light = assets.getTextureSheet("light").getSprite(2);
        lg.drawImage(
                light,
                camera.worldToScreenX(centerX),
                camera.worldToScreenY(centerY),
                (int) (width * 4), (int) (height * 4),
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
