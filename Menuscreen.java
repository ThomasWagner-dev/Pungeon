import java.io.File;
import java.util.*;
import greenfoot.*;

public class Menuscreen extends World{
    public String lastKey = "escape";
    public final GreenfootImage blackscreen;
    public final DungeonWorld origin;
    public Font title, subtitle, subsubtitle;
    public KeyLayout kl;
    public void act() {
        String key = Greenfoot.getKey();
        if (key != null && !key.equals(kl.pause)) {
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
        title = origin.fonts.get("pixel-bubble");
        subtitle = origin.fonts.get("Welbut");
        subsubtitle = origin.fonts.get("ThickThinPixel");
        this.kl = kl;
    }

    public void showKeybinds() {
        showKeybinds(kl);
    }

    public void showKeybinds(KeyLayout kl) {
        Greenfoot.setWorld(this);
        lastKey = "escape";
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
        blackscreen.drawString("press any key (except {}) to continue".replace("{}", kl.pause), 100, (getHeight()/8)*7);

        //setBackground(drawn);
    }
}
