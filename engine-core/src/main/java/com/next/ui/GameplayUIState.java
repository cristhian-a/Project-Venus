package com.next.ui;

import com.next.engine.data.Registry;
import com.next.engine.event.EventDispatcher;
import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.event.UiDamageEvent;
import com.next.model.Combatant;
import com.next.model.Player;
import com.next.system.Settings;
import com.next.engine.util.Colors;
import com.next.engine.util.Fonts;
import com.next.world.Scene;

public class GameplayUIState implements UIState {

    private Scene scene;
    private Player player;
    private final Settings.VideoSettings videoSettings;

    private final UIWorldModel uiWorldModel = new UIWorldModel();

    private String[] dialogueLines;

    private boolean dialogueActive;
    private int dialogueIndex;

    private final int fullHeart;
    private final int halfHeart;
    private final int emptyHeart;

    public GameplayUIState(Scene scene, Player player, EventDispatcher dispatcher,
                           Settings.VideoSettings videoSettings
    ) {
        this.scene = scene;
        this.player = player;
        this.videoSettings = videoSettings;

        dispatcher.register(UiDamageEvent.class, this::onFire);

        setDialogueLines();
        fullHeart = Registry.textureIds.get("heart-1.png");
        halfHeart = Registry.textureIds.get("heart-2.png");
        emptyHeart = Registry.textureIds.get("heart-3.png");
    }

    public void setDialogueLines() {
        dialogueLines = new String[4];
        dialogueLines[0] = "Hello, my name is Dummy!";
        dialogueLines[1] = "I am a test character!";
        dialogueLines[2] = "What a pretty nice camp we got in here, huh!";
        dialogueLines[3] = "Have a wonderful night!";
    }

    @Override
    public void update(double delta) {
        var bars = uiWorldModel.getHealthBars();
        var iterator = bars.values().iterator();

        while (iterator.hasNext()) {
            var healthBar = iterator.next();
            healthBar.ttl -= delta;
            if (healthBar.ttl <= 0) {
                iterator.remove();
            }
        }
    }

    @Override
    public void submitRender(RenderQueue queue) {
        renderPlayerHealth(queue);
        renderHealthBars(queue);
        if (dialogueActive) presentDialogue(queue);
    }

    public void onFire(UiDamageEvent event) {
        var bar = uiWorldModel.getHealthBars().getOrDefault(event.entityId(), new UIWorldModel.HealthBar());
        bar.entityId = event.entityId();
        bar.ttl = 10;
        uiWorldModel.getHealthBars().put(event.entityId(), bar);
    }

    private void renderPlayerHealth(RenderQueue renderQueue) {
        int hp = player.getHealth();
        int maxHp = player.getMaxHealth();

        // This calc does not allow for odd max hp values
        int fullHearts = (int) (hp * 0.5f);
        int emptyHearts = (int) ((maxHp - hp) * 0.5f);
        int halfHearts = (maxHp / 2) - (fullHearts + emptyHearts);

        int c = 0;
        int xOffset = 16;

        for (int i = 0; i < fullHearts; i++) {
            renderQueue.submit(Layer.UI_WORLD, 5 + c * xOffset, 5, fullHeart);
            c++;
        }

        for (int i = 0; i < halfHearts; i++) {
            renderQueue.submit(Layer.UI_WORLD, 5 + c * xOffset, 5, halfHeart);
            c++;
        }

        for (int i = 0; i < emptyHearts; i++) {
            renderQueue.submit(Layer.UI_WORLD, 5 + c * xOffset, 5, emptyHeart);
            c++;
        }
    }

    private void renderHealthBars(RenderQueue renderQueue) {
        var bars = uiWorldModel.getHealthBars();
        var iterator = bars.entrySet().iterator();

        while (iterator.hasNext()) {
            var entry = iterator.next();
            var entityId = entry.getKey();
            var entity = scene.getEntity(entityId);

            if (entity == null) {
                iterator.remove();
                continue;
            }

            float x = scene.camera.worldToScreenX(entity.getWorldX() - 8);
            float y = scene.camera.worldToScreenY(entity.getWorldY() - 16);

            int width = 14;
            int height = 2;
            Combatant c = (Combatant) entity;
            int oneScale = width / c.getMaxHealth();
            int sw = oneScale * c.getHealth();

            renderQueue.fillRectangle(Layer.UI_WORLD, x - 1, y - 1, width + 2, height + 2, Colors.BLACK);
            renderQueue.fillRectangle(Layer.UI_WORLD, x, y, sw, height, Colors.RED);
        }
    }

    public boolean toggleDialogue() {
        dialogueActive = !dialogueActive;

        if (!dialogueActive) {
            dialogueIndex++;
            dialogueIndex %= dialogueLines.length;
        }

        return dialogueActive;
    }

    public void presentDialogue(RenderQueue queue) {
        drawDialogue(queue);
    }

    protected void drawDialogue(RenderQueue queue) {
        int arc = 65;
        int thickness = 5;
        int x = 50, y = 500;
        int w = 925, h = 250;

        int bx = x + (thickness >> 1), by = y + (thickness >> 1);
        int bw = w - thickness + 1, bh = h - thickness + 1;
        int bArc = Math.max(0, arc - thickness);

        queue.roundStrokeRect(
                Layer.UI_SCREEN,
                x, y,
                w, h,
                thickness,
                Colors.WHITE,
                arc
        );
        queue.fillRoundRect(
                Layer.UI_SCREEN,
                bx, by,
                bw, bh,
                Colors.FADED_BLACK,
                bArc
        );

        queue.submit(
                Layer.UI_SCREEN,
                dialogueLines[dialogueIndex],
                Fonts.DEFAULT,
                Colors.WHITE,
                bx + 20,
                by + 40,
                RenderPosition.AXIS,
                0
        );
    }
}
