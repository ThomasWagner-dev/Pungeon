import greenfoot.*;

import java.io.File;
import java.util.*;
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
        ts.setBackground("Titlescreen.png");
        Greenfoot.setWorld(ts);
        Button start = new Button("start", "Start", world.awtfonts.get("Welbut").deriveFont(50F), java.awt.Color.WHITE){
            public void click_event() {
                Titlescreen ts = (Titlescreen) getWorld();
                Titlescreen.selectSave(ts.origin, ts);
            }
        };
        ts.addObject(start, ts.getWidth()/2, ts.getHeight()/8*5);
    }

    public static void selectSave(DungeonWorld world, World upper) {
        Titlescreen ts = new Titlescreen(world, upper);
        GreenfootImage blackscreen = new GreenfootImage(ts.getWidth(), ts.getHeight());
        blackscreen.setColor(Color.BLACK);
        blackscreen.fill();
        ts.setBackground(blackscreen);
        List<String> saves;
        File folder = new File(FileWork.fetchSaveFileLocation(world.runningOS)+ "/saves/");
        saves = Arrays.stream(folder.listFiles()).map(File::getName).collect(Collectors.toList());
        saves.add("New Game");
        Utils.drawCenteredText(blackscreen, java.awt.Color.WHITE, world.awtfonts.get("ThickThinPixel").deriveFont(30F), "Use left/right click to cycle save slots", ts.getWidth()/2, 400);
        Scrollfield ssb = new Scrollfield("ssb", saves, world.awtfonts.get("Welbut").deriveFont(40F), java.awt.Color.WHITE);
        ts.addObject(ssb, ts.getWidth()/2, 100);
        ts.addObject(new Button("select", "Select", world.awtfonts.get("Welbut").deriveFont(40F), java.awt.Color.WHITE){
            public void click_event() {
                Titlescreen ts = (Titlescreen) getWorld();
                Scrollfield ssb = ts.getObjects(Scrollfield.class).get(0);
                ts.origin.startSave(ssb.selected, ts);
            }
        }, ts.getWidth()/2, 500);
        Greenfoot.setWorld(ts);
    }

    public static void showNewSave(DungeonWorld origin, World upper) {
        Titlescreen ts = new Titlescreen(origin, upper);
        GreenfootImage img = new GreenfootImage(origin.menuscrn.blackscreen);
        java.awt.Font font = origin.awtfonts.get("Welbut").deriveFont(30F);
        Utils.drawRightCenteredText(img, java.awt.Color.WHITE, font, "Save name:", ts.getWidth()/2, 200);
        Utils.drawRightCenteredText(img, java.awt.Color.WHITE, font, "Your name:", ts.getWidth()/2, 250);
        Utils.drawRightCenteredText(img, java.awt.Color.WHITE, font, "Your pronouns:", ts.getWidth()/2, 300);
        Utils.drawRightCenteredText(img, java.awt.Color.WHITE, font, "Skin:", ts.getWidth()/2, 350);
        ts.addObject(new Textfield("savename", "New Save", font, java.awt.Color.WHITE, origin.inp), ts.getWidth()/2+200, 200);
        ts.addObject(new Textfield("playername", "Arva", font, java.awt.Color.WHITE, origin.inp), ts.getWidth()/2+200, 250);
        ts.addObject(new Scrollfield("pronouns", Arrays.stream(Gender.values()).map(g -> String.join("/",g.pronouns)).collect(Collectors.toList()), font, java.awt.Color.WHITE), ts.getWidth()/2+200, 300);

        ts.addObject(new Button("create", "Create new save", origin.awtfonts.get("Welbut").deriveFont(40F), java.awt.Color.WHITE){
            public void click_event() {
                Titlescreen ts = (Titlescreen) getWorld();
                List<Button> buttons = ts.getObjects(Button.class);
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
                if (savename.isBlank() || savename.equals("New Save")) {
                    System.err.println("invalid save name");
                    GreenfootImage img = new GreenfootImage(ts.getBackground());
                    Utils.drawCenteredText(img, java.awt.Color.RED, origin.awtfonts.get("Welbut").deriveFont(30F), "Invalid Savename: Name must not be blank or 'New Save'.", img.getWidth()/2, 500);
                    ts.setBackground(img);
                    return;
                }
                else if (name.isBlank()){
                    System.err.println("invalid player name");
                    GreenfootImage img = new GreenfootImage(ts.getBackground());
                    Utils.drawCenteredText(img, java.awt.Color.RED, origin.awtfonts.get("Welbut").deriveFont(30F), "Invalid playername: Name must not be blank.", img.getWidth()/2, 500);
                    ts.setBackground(img);
                    return;
                }
                FileWork.createNewSave(savename, name, pronouns, skin, ts.origin);
                ts.origin.startSave(savename, ts);
            }
        }, ts.getWidth()/2, 600);

        ts.setBackground(img);
        Greenfoot.setWorld(ts);
    }
}
