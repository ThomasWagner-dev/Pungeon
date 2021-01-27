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
    public final World upper;
    public Font title, subtitle, subsubtitle;
    public java.awt.Font awttitle, awtsubtitle, awtsubsubtitle;
    public Inputs inp;
    public final Actor returnbutton;
    public LinkedHashMap<Button, Button> buttons;

    /**
     * mainly checks if screen gets closed
     * later check if any new tab is opened
     */
    public void act() {
        //fetches the latest key pressed
        //checks if a key was pressed, and if it matches the key required to close the menu
        if (inp.checkKey(resumeKey) || Greenfoot.mouseClicked(returnbutton)) {
            on_return();
            //sets the world back to the origin, this menu was opened from
            Greenfoot.setWorld(upper);
        }
    }


    public Menuscreen(DungeonWorld origin, Inputs inp) {
        this(origin, inp, origin);
    }
    /**
     * Creates a new menuscreen
     * @param origin the world the menu will return to when closed
     * @param inp the keybinds which are gonna be shown
     */
    public Menuscreen(DungeonWorld origin, Inputs inp, World upper) {
        super(DungeonWorld.width*DungeonWorld.pixelSize, DungeonWorld.height*DungeonWorld.pixelSize, 1);
        this.upper = upper;
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
        returnbutton = createReturnbutton();
        addObject(returnbutton, getWidth()-(DungeonWorld.pixelSize/2), DungeonWorld.pixelSize/2);
    }

    /**
     * displays a fancy displayscreen of the stored keybinds
     */
    public void showKeybinds() {
        showKeybinds(inp.keybinds);
    }

    public static void showKeybinds(DungeonWorld origin, Inputs inp, World upper) {
        Menuscreen ms = new Menuscreen(origin, inp, upper);
        ms.showKeybinds();
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
        resumeKey = kl.pause;
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
        Utils.drawCenteredText(blackscreen, java.awt.Color.RED, awttitle.deriveFont(50F), text, center, 100);
        Utils.drawCenteredText(blackscreen, java.awt.Color.WHITE, awtsubtitle.deriveFont(30F), subtext, center, 250);
        Utils.drawCenteredText(blackscreen, new java.awt.Color(64,162,255), awtsubtitle.deriveFont(30F), subsubtext.replace("{}", p.displayname), center, 300);
        Utils.drawCenteredText(blackscreen, java.awt.Color.WHITE, awtsubsubtitle.deriveFont(30F), "press {} to load latest save".replace("{}", resumeKey), center, (getHeight()/8)*7);
        setBackground(blackscreen);
    }

    public static void showMap(DungeonWorld origin, Inputs inp, World upper) {
        Menuscreen ms = new Menuscreen(origin, inp, upper);
        ms.resumeKey = ms.inp.keybinds.map;
        Greenfoot.setWorld(ms);
        GreenfootImage bg = new GreenfootImage(ms.blackscreen),
            map = new GreenfootImage("images/map/maps/"+ms.origin.activeScreen.name.substring(2,ms.origin.activeScreen.name.length())+ ".png");
        double mx = bg.getWidth()/map.getWidth(), my = bg.getHeight()/map.getHeight();

        map.scale((int) Math.round(map.getWidth()*Math.min(mx, my)), (int) Math.round(map.getHeight()*Math.min(mx, my)));
        bg.drawImage(map, bg.getWidth()/2 - map.getWidth()/2,bg.getHeight()/2 - map.getHeight()/2);
        ms.setBackground(bg);
    }


    public void on_return() {}

    public void showEscape() {
        showEscape(origin, inp, upper);
    }

    public static void showEscape(DungeonWorld origin, Inputs inp, World upper) {
        Menuscreen es = new Menuscreen(origin, inp, upper);
        es.resumeKey = inp.keybinds.pause;
        es.createEscapeButtons();
        es.arrangeButtons("PAUSE","");

        Greenfoot.setWorld(es);
    }

    public void createEscapeButtons() {
        buttons = new LinkedHashMap<>();
        buttons.put(new Button("settings", "Settings", awtsubtitle.deriveFont(40F), java.awt.Color.WHITE){
            public void click_event() {
                Menuscreen es = (Menuscreen) getWorld();
                Menuscreen.showSettings(es.origin, es.inp, es, es.origin.getObjects(Player.class).get(0));
            }
        }, null);
        buttons.put(new Button("keybinds", "Keybinds", awtsubtitle.deriveFont(40F), java.awt.Color.WHITE){
            public void click_event() {
                Menuscreen es = (Menuscreen) getWorld();
                Menuscreen.showKeybinds(es.origin, es.inp, es);
            }
        }, null);
        buttons.put(new Button("map", "Pungeon map", awtsubtitle.deriveFont(40F), java.awt.Color.WHITE){
            public void click_event() {
                Menuscreen es = (Menuscreen) getWorld();
                Menuscreen.showMap(es.origin, es.inp, es);
            }
        }, null);
        buttons.put(new Button("save", "Save", awtsubtitle.deriveFont(40F), java.awt.Color.WHITE){
            public void click_event() {
                FileWork.savePlayer(origin.selectedSave, origin);
            }
        }, null);
        buttons.put(new Button("savenexit", "safe the exit", awtsubtitle.deriveFont(40F), java.awt.Color.WHITE){
            public void click_event() {
                Menuscreen ms = (Menuscreen) getWorld();
                ms.origin.save(ms.origin.selectedSave);
                Titlescreen.showTitle(ms.origin);
            }
        }, null);
    }


    public void showSettings() {
        showSettings(origin, inp, upper, origin.getObjects(Player.class).get(0));
    }

    public static void showSettings(DungeonWorld world, Inputs inp, World upper, Player p) {
        Menuscreen es = new Menuscreen(world, inp, upper){
            public void on_return() {
                world.musichandler.setVolume(Integer.parseInt(buttons.values().stream().filter(tf -> tf.name.equals("volume")).map(tf -> tf.text).findFirst().orElse("100")));
                p.displayname = buttons.values().stream().filter(tf -> tf.name.equals("name")).map(tf -> tf.text).findFirst().orElse("Arva");
                //TODO: handle monitor window placement
            }
        };
        es.resumeKey = inp.keybinds.pause;
        es.createSettingsButtons(p, world.musichandler);
        es.arrangeButtons("SETTINGS","change numeral values: left-click: increase; right-click: decrease\n shift to half changing speed");

        Greenfoot.setWorld(es);
    }

    public void createSettingsButtons(Player p, MusicHandler mh) {
        buttons = new LinkedHashMap<>();
        Button b;
        Button tf;
        b = new Button("name", "Name:", awtsubtitle.deriveFont(30F), java.awt.Color.WHITE);
        tf = new Textfield(b.name, p.displayname, b.font, b.color, false, p.inputs);
        buttons.put(b, tf);
        b = new Button("volume", "Volume:", awtsubtitle.deriveFont(30F), java.awt.Color.WHITE);
        tf = new Numberfield(b.name, mh.volume, 10, 0, 100, b.font, b.color){
            public void on_click() {
                mh.setVolume(this.value);
            }
        };
        buttons.put(b, tf);
        b = new Button("monitor", "Monitor (WIP):", awtsubtitle.deriveFont(30F), java.awt.Color.WHITE);
        tf = new Numberfield(b.name, 88, 0, 88, 88, b.font, b.color);
        buttons.put(b, tf);
    }


    public static Actor createReturnbutton() {
        Actor a = new Actor(){};
        GreenfootImage gf = new GreenfootImage("returnbutton.png");
        gf.scale(DungeonWorld.pixelSize, DungeonWorld.pixelSize);
        a.setImage(gf);
        return a;
    }

    public void arrangeButtons(String screentitle, String note) {
        GreenfootImage img = new GreenfootImage(blackscreen);
        img.setColor(greenfoot.Color.WHITE);
        img.setFont(title.deriveFont(40F));
        Utils.drawCenteredText(img, java.awt.Color.WHITE, awttitle.deriveFont(40F), screentitle, img.getWidth()/2, 50);
        //img.drawString(screentitle, 100, 50);
        int x = img.getWidth()/2, y = 100, xx;
        Button v;
        for (Button b : buttons.keySet()) {
            xx = x;
            v = buttons.get(b);
            if (v != null) {
                xx = x-(b.getImage().getWidth()/2);
                addObject(buttons.get(b), x+200, y);
            }
            addObject(b, xx, y);
            y += 70;
        }
        img.setFont(subsubtitle.deriveFont(30F));
        img.drawString(note, 100, getHeight()-50);
        setBackground(img);
    }
}
