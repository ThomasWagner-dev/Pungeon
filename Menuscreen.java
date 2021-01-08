import java.awt.*;
import javax.swing.*;
import java.io.File;
import java.util.*;
import greenfoot.*;
import greenfoot.Color;
import greenfoot.Font;
import greenfoot.util.GraphicsUtilities;

public class Menuscreen extends World{
    public String lastKey = "escape", resumeKey = null;
    public final GreenfootImage blackscreen;
    public final DungeonWorld origin;
    public Font title, subtitle, subsubtitle;
    public java.awt.Font awttitle, awtsubtitle, awtsubsubtitle;
    public KeyLayout kl;
    public void act() {
        String key = Greenfoot.getKey();
        if (key != null && (resumeKey == null || key.equals(resumeKey))) {
            System.out.println("key: "+ key + " last: " + lastKey);
            Greenfoot.setWorld(origin);
        }

        //lastKey = key==null? lastKey : key;
        lastKey = key;
    }

    public Menuscreen(DungeonWorld origin, KeyLayout kl) {
        super(DungeonWorld.width*DungeonWorld.pixelSize, DungeonWorld.height*DungeonWorld.pixelSize, 1);
        blackscreen = new GreenfootImage(getWidth(), getHeight());
        blackscreen.setColor(Color.BLACK);
        blackscreen.fill();
        setBackground(blackscreen);
        this.origin = origin;
        title = origin.gffonts.get("pixel-bubble");
        subtitle = origin.gffonts.get("Welbut");
        subsubtitle = origin.gffonts.get("ThickThinPixel");
        awttitle = origin.awtfonts.get("pixel-bubble");
        awtsubtitle = origin.awtfonts.get("Welbut");
        awtsubsubtitle = origin.awtfonts.get("ThickThinPixel");
        this.kl = kl;
    }

    public void showKeybinds() {
        showKeybinds(kl);
    }

    public void showKeybinds(KeyLayout kl) {
        Greenfoot.setWorld(this);
        lastKey = kl.pause;
        resumeKey = "r";
        GreenfootImage blackscreen = new GreenfootImage(this.blackscreen);
        blackscreen.setColor(Color.WHITE);
        int x = 100, y = 50;
        String text;
        for (KeyLayout.ActionGroup ag : KeyLayout.ActionGroup.values()) {
            text = kl.getKeysOfActionGroup(ag);
            blackscreen.setFont(title);
            blackscreen.drawString(ag.name(), x, y);
            y += 20;
            blackscreen.setFont(subtitle);
            blackscreen.drawString(text, x, y);
            y += 50;
        }
        blackscreen.drawString("press {} to continue".replace("{}", resumeKey), 100, (getHeight()/8)*7);

        setBackground(blackscreen);
    }

    public void showGameover(Player p) {
        resumeKey = "escape";
        Greenfoot.setWorld(this);

        String text = "GAME OVER", subtext = "You died", subsubtext = "Come on {}, you can do it. Stay determent";
        int center = blackscreen.getWidth()/2;
        GreenfootImage blackscreen = new GreenfootImage(this.blackscreen);
        drawCenteredText(blackscreen, java.awt.Color.RED, awttitle.deriveFont(50F), text, center, 100);
        drawCenteredText(blackscreen, java.awt.Color.WHITE, awtsubtitle.deriveFont(30F), subtext, center, 250);
        drawCenteredText(blackscreen, java.awt.Color.WHITE, awtsubsubtitle.deriveFont(30F), "press {} to load latest save".replace("{}", resumeKey), center, (getHeight()/8)*7);
        setBackground(blackscreen);
        origin.removeObjects(origin.getObjectsExclusive(Counter.class));
        FileWork.loadPlayer(origin.selectedSave, origin, p);
    }


    /**
     * @param gfi  The GreenfootImage to center the text on
     * @param text The text to draw
     * @param x    the x coord to center around, use negative (out of bounds) values to place in middle of image
     * @param y    the y coord to center around, use negative (out of bounds) values to place in middle of image
     */
    public static void drawCenteredText(GreenfootImage gfi, java.awt.Color color, java.awt.Font font, String text, int x, int y)
    {
        Graphics2D graphics = gfi.getAwtImage().createGraphics();
        if (graphics == null)
        {
            return;
        }

        graphics.setColor(color);
        graphics.setFont(font);

        if (x < 0 || x >= gfi.getWidth())
        {
            x = gfi.getWidth() / 2;
        }
        if (y < 0 || y >= gfi.getHeight())
        {
            y = gfi.getHeight() / 2;
        }

        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        String[] lines = GraphicsUtilities.splitLines(text);
        FontMetrics fontMetrics = graphics.getFontMetrics(font);
        int height = fontMetrics.getHeight();
        y -= height * (lines.length / 2);
        y -= height / 2;
        for (int i = 0; i < lines.length; ++i)
        {
            graphics.drawString(lines[i], x - (fontMetrics.stringWidth(lines[i]) / 2), y + i * height);
        }

        graphics.dispose();
    }
}
