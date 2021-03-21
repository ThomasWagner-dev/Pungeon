
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class Titlescreen extends World {

    public DungeonWorld origin;
    public World upper;

    public Titlescreen(DungeonWorld origin, World upper) {
        super(DungeonWorld.width*DungeonWorld.pixelSize, DungeonWorld.height*DungeonWorld.pixelSize, 1);
        this.upper = upper;
        this.origin = origin;
    }

    public static void showTitle(DungeonWorld world) {
        Titlescreen ts = new Titlescreen(world, world);
        ts.setBackground(Utils.loadImageFromAssets("Titlescreen.png"));
        switchWorld(ts);
        Button start = new Button(){
            public void tick(){}
            public void clickEvent(MouseEventInfo e) {
                Titlescreen ts = (Titlescreen) world;
                Titlescreen.selectSave(ts.origin, ts);
            }
        };
        start.name = "start";
        start.setText("Start");
        start.setFont(world.awtfonts.get("Welbut").deriveFont(50F));
        start.setTextColor(Color.WHITE);
        start.setBackgroundColor(Color.BLACK);

        ts.addObject(start, ts.getWidth()/2, ts.getHeight()/8*5);
    }

    public static void selectSave(DungeonWorld world, World upper) {
        Titlescreen ts = new Titlescreen(world, upper);
        AdvancedImage blackscreen = new AdvancedImage(ts.getWidth(), ts.getHeight());
        blackscreen.fill(Color.BLACK);
        ts.setBackground(blackscreen);
        List<String> saves;
        File folder = new File(FileWork.fetchSaveFileLocation(world.runningOS)+ "/saves/");
        saves = Arrays.stream(folder.listFiles()==null?new File[0]:folder.listFiles()).map(File::getName).collect(Collectors.toList());
        saves.add("New Game");
        blackscreen.drawText(java.awt.Color.WHITE, world.awtfonts.get("ThickThinPixel").deriveFont(30F), "Use left/right click to cycle save slots", ts.getWidth()/2, 400);
        ScrollButton ssb = new ScrollButton(saves, saves.get(0));
        ssb.name = "ssb";
        ssb.setFont(world.awtfonts.get("Welbut").deriveFont(40F));
        ssb.setTextColor(Color.WHITE);
        ssb.setBackgroundColor(Color.BLACK);
        ts.addObject(ssb, ts.getWidth()/2, 100);
        Button tmp = new Button() {
            @Override
            public void clickEvent(MouseEventInfo mouseEventInfo) {
                Titlescreen ts = (Titlescreen) world;
                ScrollButton ssb = ts.objectsOf(ScrollButton.class).get(0);
                ts.origin.startSave((String) ssb.selected, ts);
            }

            @Override
            public void tick() {

            }
        };
        tmp.name = "select";
        tmp.setText("Select");
        tmp.setFont(world.awtfonts.get("Welbut").deriveFont(40F));
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        ts.addObject(tmp, ts.getWidth()/2, 500);
        switchWorld(ts);
    }

    public static void showNewSave(DungeonWorld origin, World upper) {
        Titlescreen ts = new Titlescreen(origin, upper);
        AdvancedImage img = new AdvancedImage(ts.width, ts.height);
        img.fill(Color.BLACK);
        java.awt.Font font = origin.awtfonts.get("Welbut").deriveFont(30F);
        img.drawText(java.awt.Color.WHITE, font, "Save name:", ts.getWidth()/2, 200, AdvancedImage.VerticalAlignment.CENTER, AdvancedImage.HorizontalAlignment.RIGHT);
        img.drawText(java.awt.Color.WHITE, font, "Your name:", ts.getWidth()/2, 250, AdvancedImage.VerticalAlignment.CENTER, AdvancedImage.HorizontalAlignment.RIGHT);
        img.drawText(java.awt.Color.WHITE, font, "Your pronouns:", ts.getWidth()/2, 300, AdvancedImage.VerticalAlignment.CENTER, AdvancedImage.HorizontalAlignment.RIGHT);
        img.drawText(java.awt.Color.WHITE, font, "Skin:", ts.getWidth()/2, 350, AdvancedImage.VerticalAlignment.CENTER, AdvancedImage.HorizontalAlignment.RIGHT);
        Button tmp = new Textfield();
        tmp.name = "savename";
        tmp.setText("New Save");
        tmp.setFont(font);
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        tmp.setSize(200,50);
        ts.addObject(tmp, ts.getWidth()/2+200, 200);
        tmp = new Textfield();
        tmp.name = "playername";
        tmp.setText("Arva");
        tmp.setFont(font);
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        tmp.setSize(200,50);
        ts.addObject(tmp, ts.getWidth()/2+200, 250);
        List<String> options = Arrays.stream(Gender.values()).map(g -> String.join("/",g.pronouns)).collect(Collectors.toList());
        tmp = new ScrollButton(options, options.get(0));
        tmp.name = "pronouns";
        tmp.setFont(font);
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        tmp.setSize(200,50);
        ts.addObject(tmp, ts.getWidth()/2+200, 300);
        tmp = new Button() {
            @Override
            public void clickEvent(MouseEventInfo mouseEventInfo) {
                Titlescreen ts = (Titlescreen) world;
                List<Button> buttons = ts.objectsOf(Button.class);
                String name="", savename="", pronouns="", skin = "";
                for (Button b : buttons) {
                    switch (b.name) {
                        case "savename":
                            savename = b.text.replace("/","").replace("\\", "");
                            break;
                        case "playername":
                            name = b.text;
                            break;
                        case "pronouns":
                            pronouns = b.text;
                            break;
                    }
                }
                savename = savename.trim();
                if (savename.trim().isEmpty() || savename.equals("New Save")) {
                    System.err.println("invalid save name");
                    AdvancedImage img = new AdvancedImage(ts.getBackground());
                    img.drawText(java.awt.Color.RED, origin.awtfonts.get("Welbut").deriveFont(30F), "Invalid Savename: Name must not be blank or 'New Save'.", img.getWidth()/2, 500);
                    ts.setBackground(img);
                    return;
                }
                else if (name.trim().isEmpty()){
                    System.err.println("invalid player name");
                    AdvancedImage img = new AdvancedImage(ts.ui.backgroundImage);
                    img.drawText(java.awt.Color.RED, origin.awtfonts.get("Welbut").deriveFont(30F), "Invalid playername: Name must not be blank.", img.getWidth()/2, 500);
                    ts.setBackground(img);
                    return;
                }
                FileWork.createNewSave(savename, name, pronouns, skin, ts.origin);
                ts.origin.startSave(savename, ts);

            }

            @Override
            public void tick() {

            }
        };
        tmp.name = "create";
        tmp.setText("Create new save");
        tmp.setFont(origin.awtfonts.get("Welbut").deriveFont(40F));
        tmp.setTextColor(Color.WHITE);
        tmp.setBackgroundColor(Color.BLACK);
        ts.addObject(tmp, ts.width/2, 600);

        ts.setBackground(img);
        switchWorld(ts);
    }

    public void tick() {}
}
