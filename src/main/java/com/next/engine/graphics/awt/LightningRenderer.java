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

        BufferedImage light = assets.getTextureSheet("light").getSprite(0);
        lg.drawImage(
                light,
                camera.worldToScreenX(368) - 16 / 2,
                camera.worldToScreenY(336) - 16 / 2,
                32, 32,
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
