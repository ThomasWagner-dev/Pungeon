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
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
            super(1425, 850, 1); 
        dmgMultiplier = FileWork.getDmgMultiplier();
    }
    
    /**
     * unloads the current screen and load in the new Screen.
     * 
     * TODO
     */
    public void loadScreen(Object map) {
        removeObjects(getObjects(null));
        
    }
}