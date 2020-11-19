import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.*;

/**
 * This is the world...
 * 
 * @author Commentator
 */
public class DungeonWorld extends World
{
    public final HashMap<String, HashMap<String, Double>> dmgMultiplier;
    public DungeonWorld()
    {    
        // Create a new world with 1425x850 cells with a cell size of 1x1 pixels.
        super(1425, 850, 1);
        //Load dmgMultipliers.
        dmgMultiplier = FileWork.getDmgMultiplier();
    }

    /**
     * unloads the current screen and load in the new Screen.
     * 
     * TODO
     */
    public void loadScreen(String screenName) {
        //Remove all Existing Objects
        removeObjects(getObjects(null));
        //Spawn Loading Screen.
        Tile loadingScreen = new Tile();
        addObject(loadingScreen, 712, 425);
        loadingScreen.setImage("Wall.jpg"); 
        loadingScreen.getImage().scale(1425, 850);
        setPaintOrder(Tile.class);
        //Load the world from a file.
        int[][] world = FileWork.loadWorldFile(screenName);
        //Render the world.
        int row = 0;
        //Load and add all Actors to the world.
        while (row < world.length) {
            for (int block : world[row]) {
                addObject((Actor)FileWork.loadBlock(block), 0, 0);// TODO think about a tile and wall size.
            }
            row++;
        }
        removeObject(loadingScreen);
        setPaintOrder(Player.class, Enemy.class, Projectile.class);//TODO Update paintorder after being done.
    }
}