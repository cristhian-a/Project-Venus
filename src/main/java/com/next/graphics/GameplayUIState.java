package com.next.graphics;

import com.next.engine.graphics.Layer;
import com.next.engine.graphics.RenderPosition;
import com.next.engine.graphics.RenderQueue;
import com.next.model.Player;
import com.next.system.Settings;
import com.next.util.Colors;
import com.next.util.Fonts;

public class GameplayUIState implements UIState {

    private Player player;
    private final Settings.VideoSettings videoSettings;

    private String[] dialogueLines;

    private boolean dialogueActive;
    private int dialogueIndex;

    public GameplayUIState(Player player, Settings.VideoSettings videoSettings) {
        this.player = player;
        this.videoSettings = videoSettings;

        setDialogueLines();
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
    }

    @Override
    public void submitRender(RenderQueue queue) {
        renderHealth(queue);
        if (dialogueActive) presentDialogue(queue);
    }

    private void renderHealth(RenderQueue renderQueue) {
        int hp = player.getHealth();
        int maxHp = player.getMaxHp();

        // This calc does not allow for odd max hp values
        int fullHearts = (int) (hp * 0.5f);
        int emptyHearts = (int) ((maxHp - hp) * 0.5f);
        int halfHearts = (maxHp / 2) - (fullHearts + emptyHearts);

        int c = 0;
        int xOffset = 4 * videoSettings.SCALE;

        for (int i = 0; i < fullHearts; i++) {
            renderQueue.submit(Layer.UI, 5 + c * xOffset, 5, 36);
            c++;
        }

        for (int i = 0; i < halfHearts; i++) {
            renderQueue.submit(Layer.UI, 5 + c * xOffset, 5, 37);
            c++;
        }

        for (int i = 0; i < emptyHearts; i++) {
            renderQueue.submit(Layer.UI, 5 + c * xOffset, 5, 38);
            c++;
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
                Layer.UI,
                x, y,
                w, h,
                thickness,
                Colors.WHITE,
                arc
        );
        queue.fillRoundRect(
                Layer.UI,
                bx, by,
                bw, bh,
                Colors.FADED_BLACK,
                bArc
        );

        queue.submit(
                Layer.UI,
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
