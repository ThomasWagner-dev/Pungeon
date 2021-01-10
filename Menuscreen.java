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
    public Inputs inp;

    /**
     * mainly checks if screen gets closed
     * later check if any new tab is opened
     */
    public void act() {
        //fetches the latest key pressed
        //checks if a key was pressed, and if it matches the key required to close the menu
        if (inp.checkKey(resumeKey)) {
            //sets the world back to the origin, this menu was opened from
            Greenfoot.setWorld(origin);
        }
    }

    /**
     * Creates a new menuscreen
     * @param origin the world the menu will return to when closed
     * @param inp the keybinds which are gonna be shown
     */
    public Menuscreen(DungeonWorld origin, Inputs inp) {
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
        this.inp = inp;
    }

    /**
     * displays a fancy displayscreen of the stored keybinds
     */
    public void showKeybinds() {
        showKeybinds(inp.keybinds);
    }

    /**
     * shows a fancy screen showing the keybinds
     * @param kl the keybins which are gonna be displayed
     */
    public void showKeybinds(KeyLayout kl) {
        //sets the active world to this. therefore shows this screen
        Greenfoot.setWorld(this);
        //sets the button to close the screen to r
        lastKey = kl.pause;
        resumeKey = "r";
        //clones the image to be able to write on it without interfearing with the original screen
        GreenfootImage blackscreen = new GreenfootImage(this.blackscreen);
        //sets color one writes as to white
        blackscreen.setColor(Color.WHITE);
        int x = 100, y = 50;
        String text;
        //loops though all actiongroups the keylayout has
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

    /**
     * shows a simple gameover screen.
     * @param p the player which died.
     *
     */
    public void showGameover(Player p) {
        resumeKey = "escape";
        Greenfoot.setWorld(this);

        String text = "GAME OVER", subtext = "You died", subsubtext = "Come on {}, you can do it. Stay determined!";
        int center = blackscreen.getWidth()/2;
        GreenfootImage blackscreen = new GreenfootImage(this.blackscreen);
        drawCenteredText(blackscreen, java.awt.Color.RED, awttitle.deriveFont(50F), text, center, 100);
        drawCenteredText(blackscreen, java.awt.Color.WHITE, awtsubtitle.deriveFont(30F), subtext, center, 250);
        drawCenteredText(blackscreen, new java.awt.Color(64,162,255), awtsubtitle.deriveFont(30F), subsubtext.replace("{}", p.displayname), center, 300);
        drawCenteredText(blackscreen, java.awt.Color.WHITE, awtsubsubtitle.deriveFont(30F), "press {} to load latest save".replace("{}", resumeKey), center, (getHeight()/8)*7);
        setBackground(blackscreen);
    }

    public void showMap() {
        resumeKey = "escape";
        Greenfoot.setWorld(this);
        GreenfootImage bg = new GreenfootImage(blackscreen),
            map = new GreenfootImage("images/map/maps/"+origin.activeScreen.name.substring(2,origin.activeScreen.name.length())+ ".png");
        double mx = bg.getWidth()/map.getWidth(), my = bg.getHeight()/map.getHeight();

        map.scale((int) Math.round(map.getWidth()*Math.min(mx, my)), (int) Math.round(map.getHeight()*Math.min(mx, my)));
        bg.drawImage(map, bg.getWidth()/2 - map.getWidth()/2,bg.getHeight()/2 - map.getHeight()/2);
        setBackground(bg);
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
