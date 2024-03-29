import java.awt.*;
import java.util.*;

public class Menuscreen extends World{
    public String lastKey = "escape", resumeKey = null;
    public final AdvancedImage blackscreen;
    public final DungeonWorld origin;
    public final World upper;
    public Font title, subtitle, subsubtitle;
    public java.awt.Font awttitle, awtsubtitle, awtsubsubtitle;
    public Inputs inp;
    public final WorldObj returnbutton;
    public LinkedHashMap<Button, Button> buttons;

    /**
     * mainly checks if screen gets closed
     * later check if any new tab is opened
     */
    public void tick() {
    }

    public void leave() {
        on_return();
        switchWorld(upper);
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
        blackscreen = new AdvancedImage(1, 1);
        blackscreen.imgs = AdvancedImage.ImageSizing.STRETCH;
        blackscreen.fill(Color.BLACK);
        setBackground(blackscreen);
        this.origin = origin;
        title = origin.awtfonts.get("pixel-bubble");
        subtitle = origin.awtfonts.get("Welbut");
        subsubtitle = origin.awtfonts.get("ThickThinPixel");
        awttitle = origin.awtfonts.get("pixel-bubble");
        awtsubtitle = origin.awtfonts.get("Welbut");
        awtsubsubtitle = origin.awtfonts.get("ThickThinPixel");
        this.inp = inp;
        returnbutton = createReturnbutton();
        addObject(returnbutton, width-(DungeonWorld.pixelSize/2), DungeonWorld.pixelSize/2);
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
        //sets the tickive world to this. therefore shows this screen
        switchWorld(this);
        //sets the button to close the screen to r
        lastKey = kl.pause+"";
        resumeKey = kl.pause+"";
        //clones the image to be able to write on it without interfearing with the original screen
        AdvancedImage blackscreen = new AdvancedImage(width, height);
        blackscreen.fill(Color.BLACK);
        //sets color one writes as to white
        int x = width/2, y = 50;
        String text;
        //loops though all tickiongroups the keylayout has
        for (KeyLayout.tickionGroup ag : KeyLayout.tickionGroup.values()) {
            text = kl.getKeysOftickionGroup(ag);
            blackscreen.drawText(Color.WHITE, title.deriveFont(30F), ag.name(), x, y);
            y += 30;
            blackscreen.drawText(Color.WHITE, subtitle.deriveFont(30F), text, x, y);
            y += 50;
        }
        blackscreen.drawText(Color.WHITE, subtitle.deriveFont(30F), "press escape to continue".replace("{}", resumeKey), width/2, (height/8)*7);
        setBackground(blackscreen);
    }

    /**
     * shows a simple gameover screen.
     * @param p the player which died.
     *
     */
    public void showGameover(Player p) {
        resumeKey = "escape";
        switchWorld(this);

        String text = "GAME OVER", subtext = "You died", subsubtext = "Come on {}, you made it to stage {1}. Stay determined!";
        int center = getWidth()/2;

        AdvancedImage blackscreen = new AdvancedImage(width,height);
        blackscreen.fill(Color.BLACK);
        blackscreen.drawText(java.awt.Color.RED, awttitle.deriveFont(50F), text, center, 100);
        blackscreen.drawText(java.awt.Color.WHITE, awtsubtitle.deriveFont(30F), subtext, center, 250);
        blackscreen.drawText(new java.awt.Color(64,162,255), awtsubtitle.deriveFont(30F), subsubtext.replace("{}", p.displayname).replace("{1}", ((DungeonWorld)p.world).mg.level-2+""), center, 300);
        blackscreen.drawText(java.awt.Color.WHITE, awtsubsubtitle.deriveFont(30F), "press {} to reload this level".replace("{}", resumeKey), center, (height/8)*7);
        setBackground(blackscreen);
    }

    public static void showMap(DungeonWorld origin, Inputs inp, World upper) {
        Menuscreen ms = new Menuscreen(origin, inp, upper);
        ms.resumeKey = ms.inp.keybinds.map+"";
        switchWorld(ms);
        AdvancedImage bg = new AdvancedImage(ms.width, ms.height),
            map = Utils.loadImageFromAssets("map/maps/" +ms.origin.tickiveScreen.name.substring(2)+ ".png");
        bg.fill(Color.BLACK);
        double mx = bg.getWidth()/map.getWidth(), my = bg.getHeight()/map.getHeight();

        map = map.scale((int) Math.round(map.getWidth()*Math.min(mx, my)), (int) Math.round(map.getHeight()*Math.min(mx, my)));
        bg.drawImage(map, bg.getWidth()/2 - map.getWidth()/2,bg.getHeight()/2 - map.getHeight()/2);
        ms.setBackground(bg);
    }


    public void on_return() {}

    public void showEscape() {
        showEscape(origin, inp, upper);
    }

    public static void showEscape(DungeonWorld origin, Inputs inp, World upper) {
        Menuscreen es = new Menuscreen(origin, inp, upper);
        es.resumeKey = inp.keybinds.pause + "";
        es.createEscapeButtons();
        es.arrangeButtons("PAUSE","");

        switchWorld(es);
    }

    public void createEscapeButtons() {
        buttons = new LinkedHashMap<>();
        Button tmp = new Button() {
            public void tick() {
            }

            public void clickEvent(MouseEventInfo e) {
                Menuscreen es = (Menuscreen) world;
                Menuscreen.showSettings(es.origin, es.inp, es, es.origin.objectsOf(Player.class).get(0));
            }
        };
        tmp.name = "settings";
        tmp.setText("Settings");
        tmp.setFont(awtsubtitle.deriveFont(40F));
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);

        buttons.put(tmp, null);
        tmp = new Button(){
            public void tick(){}
            public void clickEvent(MouseEventInfo e) {
                Menuscreen es = (Menuscreen) world;
                Menuscreen.showKeybinds(es.origin, es.inp, es);
            }
        };
        tmp.name = "keybinds";
        tmp.setText("Keybinds");
        tmp.setFont(awtsubtitle.deriveFont(40F));
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        buttons.put(tmp, null);
        tmp = new Button(){
            public void tick(){}
            public void clickEvent(MouseEventInfo e) {
                Menuscreen es = (Menuscreen) world;
                Menuscreen.showMap(es.origin, es.inp, es);
            }
        };
        tmp.name = "map";
        tmp.setText("Pungeon map");
        tmp.setFont(awtsubtitle.deriveFont(40F));
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        buttons.put(tmp, null);
        tmp = new Button(){
            public void tick(){}
            public void clickEvent(MouseEventInfo e) {
                FileWork.savePlayer(origin.selectedSave, origin);
            }
        };
        tmp.name = "save";
        tmp.setText("Save");
        tmp.setFont(awtsubtitle.deriveFont(40F));
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        buttons.put(tmp, null);

        tmp = new Button() {
            @Override
            public void clickEvent(MouseEventInfo mouseEventInfo) {
                Menuscreen ms = (Menuscreen) world;
                ms.showCredits();
            }

            @Override
            public void tick() {

            }
        };
        tmp.name = "credits";
        tmp.setText("Credits");
        tmp.setFont(awtsubtitle.deriveFont(40F));
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        buttons.put(tmp,null);

        tmp = new Button(){
            public void tick(){}
            public void clickEvent(MouseEventInfo e) {
                Menuscreen ms = (Menuscreen) world;
                ms.origin.save(ms.origin.selectedSave);
                Titlescreen.showTitle(ms.origin);
            }
        };
        tmp.name = "savenexit";
        tmp.setText("Safe the exit");
        tmp.setFont(awtsubtitle.deriveFont(40F));
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        buttons.put(tmp, null);
    }


    public void showSettings() {
        showSettings(origin, inp, upper, origin.objectsOf(Player.class).get(0));
    }

    public static void showSettings(DungeonWorld world, Inputs inp, World upper, Player p) {
        Menuscreen es = new Menuscreen(world, inp, upper){
            public void on_return() {
                world.musichandler.setVolume(Integer.parseInt(buttons.values().stream().filter(tf -> tf.name.equals("volume")).map(tf -> tf.text).findFirst().orElse("100")));
                p.displayname = buttons.values().stream().filter(tf -> tf.name.equals("name")).map(tf -> tf.text).findFirst().orElse("Arva");
                mainframe.setFramePosition(buttons.values().stream().filter(tf -> tf.name.equals("monitor")).filter(tf -> !tf.text.equals("")).map(tf -> Integer.parseInt(tf.text)).findFirst().orElse(0));
                //TODO: handle monitor window placement
            }
        };
        es.resumeKey = inp.keybinds.pause+ "";
        es.createSettingsButtons(p, world.musichandler);
        es.arrangeButtons("SETTINGS","");

        switchWorld(es);
    }

    public void createSettingsButtons(Player p, MusicBox mh) {
        buttons = new LinkedHashMap<>();
        Button b;
        Textfield tf;
        b = new Button() {
            @Override
            public void clickEvent(MouseEventInfo mouseEventInfo) {

            }

            @Override
            public void tick() {

            }
        };
        b.name = "name";
        b.setText("Name:");
        b.setFont(awtsubtitle.deriveFont(30F));
        b.setTextColor(Color.WHITE);
        tf = new Textfield();
        tf.name = b.name;
        tf.text = p.displayname;
        tf.font = b.font;
        tf.setTextColor(b.textColor);
        b.setBackgroundColor(Color.BLACK);
        tf.setBackgroundColor(Color.BLACK);
        buttons.put(b, tf);
        b = new Button() {
            @Override
            public void clickEvent(MouseEventInfo mouseEventInfo) {

            }

            @Override
            public void tick() {

            }
        };
        b.name = "volume";
        b.setText("Volume:");
        b.setFont(awtsubtitle.deriveFont(30F));
        b.setTextColor(Color.WHITE);
        tf = new Textfield();
        tf.name = b.name;
        tf.setText(mh.volume+"");
        tf.isNumeric = true;
        tf.maxvalue = 100;
        tf.setFont(b.font);
        tf.setTextColor(b.textColor);
        b.setBackgroundColor(Color.BLACK);
        tf.setBackgroundColor(Color.BLACK);
        buttons.put(b, tf);
        b = new Button() {
            @Override
            public void clickEvent(MouseEventInfo mouseEventInfo) {

            }

            @Override
            public void tick() {

            }
        };
        b.name = "monitor";
        b.setText("Monitor:");
        b.setFont(awtsubtitle.deriveFont(30F));
        b.setTextColor(Color.WHITE);
        tf = new Textfield() {

        };
        tf.name = b.name;
        int ma = mainframe.getMonitorCount();
        tf.setFont(b.font);
        tf.setTextColor(b.textColor);
        tf.isNumeric = true;
        tf.maxvalue = ma;
        b.setBackgroundColor(Color.BLACK);
        tf.setBackgroundColor(Color.BLACK);
        tf.setText(mainframe.selectedMonitor+"");
        buttons.put(b, tf);
    }


    public static WorldObj createReturnbutton() {
        WorldObj a = new Button(){
            @Override
            public void tick() {

            }
            public void clickEvent(MouseEventInfo e) {
                ((Menuscreen) world).leave();
            }
        };
        AdvancedImage gf = Utils.loadImageFromAssets("returnbutton.png");
        gf = gf.scale(DungeonWorld.pixelSize, DungeonWorld.pixelSize);
        a.setImage(gf);
        return a;
    }

    public void arrangeButtons(String screentitle, String note) {
        AdvancedImage img = new AdvancedImage(width, height);
        img.fill(Color.BLACK);
        img.drawText(java.awt.Color.WHITE, awttitle.deriveFont(40F), screentitle, img.getWidth()/2, 50);
        //img.drawString(screentitle, 100, 50);
        int x = img.getWidth()/2, y = 100, xx;
        Button v;
        for (Button b : buttons.keySet()) {
            xx = x;
            v = buttons.get(b);
            b.setSize(300,50);
            if (v != null) {
                v.setSize(300,50);
                xx = x-(b.img.getWidth()/2);
                addObject(buttons.get(b), x+200, y);
            }
            addObject(b, xx, y);
            y += 70;
        }
        img.drawText(Color.WHITE, subsubtitle.deriveFont(30F), note, 100, height-50);
        setBackground(img);
    }

    public void keyPressed(char key) {
        if ("escape".equals(resumeKey) && key == 27)
            leave();

        else if ("tab".equals(resumeKey) && key == 9)
            leave();

        else if ((key+"").equals(resumeKey))
            leave();
    }

    public void showNextLevel() {
        AdvancedImage img = new AdvancedImage(width, height);
        img.fill(Color.BLACK);
        img.drawText(Color.BLUE, title.deriveFont(40F), "CONGRATULATIONS", width/2, 50);
        img.drawText(Color.WHITE, subtitle.deriveFont(30F), "You managed to escape this floor of the dungeon", width/2, 100);
        img.drawText(Color.RED, subtitle.deriveFont(30F), "But only to go deeper into it.", width/2, 170);

        Button tmp = new Button() {
            @Override
            public void clickEvent(MouseEventInfo mouseEventInfo) {
                Menuscreen ts = (Menuscreen) world;
                DungeonWorld w = ts.origin;
                w.mg = new MapGenerator(world.random, new ArrayList<>(w.rawscreens.values()), w, w.mg.level+1);
                w.mg.getAt(0,0).load(w);
                World.switchWorld(w);
                ts.removeAll();
            }

            @Override
            public void tick() {

            }
        };
        tmp.setBackgroundColor(Color.BLACK);
        tmp.setBorderColor(Color.BLUE);
        tmp.setTextColor(Color.GREEN);
        tmp.setFont(subsubtitle.deriveFont(30F));
        tmp.setSize(300,50);
        tmp.setText("Enter floor "+ (origin.mg.level-1));
        addObject(tmp, width/2, height/8*7);

        removeObject(returnbutton);

        setBackground(img);
        switchWorld(this);
    }

    public void showCredits() {
        removeAll();
        resumeKey = "escape";
        AdvancedImage img = new AdvancedImage(width, height);
        img.fill(Color.BLACK);

        img.drawText(Color.WHITE, awttitle.deriveFont(50F), "CREDITS", width/2, 50);

        img.drawText(Color.GREEN, awttitle.deriveFont(30F), "CODE", width/4, 150);
        img.drawText(Color.GREEN, awtsubtitle.deriveFont(20F), "Commentator2.0", width/4, 200);
        img.drawText(Color.GREEN, awtsubtitle.deriveFont(20F), "ThomasWagner-dev", width/4, 250);

        img.drawText(Color.WHITE, awttitle.deriveFont(30F), "MUSIC", width/4, 350);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "Jimmy#7514", width/4, 400);

        img.drawText(Color.WHITE, awttitle.deriveFont(30F), "SPRITES", width/4, 500);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "Klaus", width/4, 550);

        img.drawText(Color.WHITE, awttitle.deriveFont(30F), "SOUNDS", width/4, 650);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "Commentator2.0", width/4, 700);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "WilsontheWolf", width/4, 750);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "created with https://sfxr.me/", width/4, 800);


        img.drawText(Color.WHITE, awttitle.deriveFont(30F), "LIBRARIES", width/4*3, 150);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "Scisneromam (Connected textures)", width/4*3, 200);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "Udoprog (Minecraft TAG class)", width/4*3, 250);

        img.drawText(Color.WHITE, awttitle.deriveFont(30F), "FONT", width/4*3, 350);
        img.drawText(Color.WHITE, awttitle.deriveFont(20F), "JWIGGS", width/4*3, 400);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "Karen Clemens",width/4*3, 450);
        img.drawText(Color.WHITE, awtsubsubtitle.deriveFont(20F), "Devil's Work.shop", width/4*3, 500);

        img.drawText(Color.WHITE, awttitle.deriveFont(30F), "SPECIAL THANKS", width/4*3, 600);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "democat (playtest mac)", width/4*3, 650);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(20F), "WilsontheWolf (playtest Linux)", width/4*3, 700);
        img.drawText(Color.WHITE, awtsubtitle.deriveFont(40F), "YOU for playing this game", width/4*3, 750);


        setBackground(img);
        switchWorld(this);
    }
}
