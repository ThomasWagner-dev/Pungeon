import java.io.File;
import java.util.*;
import greenfoot.*;

public class Menuscreen extends World{
    public String lastKey = "escape";
    public final GreenfootImage blackscreen;
    public final DungeonWorld origin;
    public void act() {
        String key = Greenfoot.getKey();
        if (key != null && !key.equals("escape")) {
            System.out.println("key: "+ key + " last: " + lastKey);
            Greenfoot.setWorld(origin);
        }

        //lastKey = key==null? lastKey : key;
        lastKey = key;
    }

    public Menuscreen(DungeonWorld origin) {
        super(DungeonWorld.width*DungeonWorld.pixelSize, DungeonWorld.height*DungeonWorld.pixelSize, 1);
        blackscreen = new GreenfootImage(getWidth(), getHeight());
        blackscreen.setColor(Color.BLACK);
        blackscreen.fill();
        setBackground(blackscreen);
        this.origin = origin;
    }

    public void showKeybinds(KeyLayout kl) {
        Greenfoot.setWorld(this);
        lastKey = "escape";
        blackscreen.setColor(Color.WHITE);
        int x = 100, y = 50;
        blackscreen.setFont(origin.fonts.get("ThickThinPixel"));
        String text;
        for (KeyLayout.ActionGroup ag : KeyLayout.ActionGroup.values()) {
            text = kl.getKeysOfActionGroup(ag);
            blackscreen.drawString(ag.name(), x, y);
            y += 20;
            blackscreen.drawString(text, x, y);
            y += 50;
        }
        blackscreen.drawString("press any key (except escape) to continue", 100, (getHeight()/8)*7);

        //setBackground(drawn);
    }
}
