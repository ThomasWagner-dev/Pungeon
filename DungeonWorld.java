 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * This is the world...
 * 
 * @author Commentator
 */
public class DungeonWorld extends World
{
    public final HashMap<String, HashMap<String, Double>> dmgMultiplier;
    public static HashMap<String, Weapon> weapons;
    protected final HashMap<String, Block> blocks;
    public static final int pixelSize = 64, globalScale=pixelSize/16, height = 13, width=22;
    public String activeScreen;
    /**
     * Simple constructor to create the lobby containing Dungeon selection etc.
     */
    public DungeonWorld()
    {    
        // Create a new world with 1425x850 cells with a cell size of 1x1 pixels.
        super(width*pixelSize, height*pixelSize, 1);
        // Inform the player of the loading process.
        System.out.println("Starting world generation...");
        // Load damage Multipliers.
        System.out.println("Loading damage multipliers...");
        dmgMultiplier = FileWork.getDmgMultiplier();
        //Load weapons
        weapons = FileWork.loadAllWeapons();
        System.out.println(weapons.keySet());
        // Load all blocks availabel for map generation.
        System.out.println("Loading blocks...");
        blocks = FileWork.loadAllBlocks();
        // Load the world.
        System.out.println("Loading world...");
        FileWork.loadPlayer(0, this);
        
        // Inform the player of the end of the loading process.
        System.out.println("Finished loading.");
    }

    /**
     * Unload the current world and then load a new one.
     * 
     * @param screenName The name of the screen to be loaded.
     */
    public void loadScreen(String screenName) {
        // Remove all Existing Objects
        removeObjects(getObjects(null));
        // Spawn Loading Screen.
        setPaintOrder(Tile.class);
        // Load the world from a file.
        loadMap(screenName);
        loadEnemies(screenName);
        
        activeScreen = screenName;
        //System.out.println(activeScreen);
        setPaintOrder(Player.class, Enemy.class, Projectile.class);
    }
    
    private void loadMap(String screenName) {
        ArrayList<ArrayList<String>> world = FileWork.loadWorldFile(screenName);
        
        // Render the world.
        int row = 0;
        // Load and add all Actors to the world.
        while (row < world.size()) {
            for (int i = 0; i < world.get(row).size(); i++) {
                addObject(
                    blocks.get(
                        world.get(row).get(i)).clone(),
                        i * pixelSize + pixelSize/2,
                        row * pixelSize + pixelSize/2);
            }
            row++;
        }
        
    }
    
    private void loadEnemies(String screenName) {
        HashMap<Enemy, int[]> enemies = FileWork.loadEnemyFile(screenName);
        int[] pos;
        for (Enemy e : enemies.keySet()) {
            pos = enemies.get(e);
            addObject(e, pixelSize/2+pos[0]*pixelSize, pixelSize/2+pos[1]*pixelSize);
        }
    }
    
    /**
     * Gets the closest Object of a specific class to the calling Actor.
     * 
     * @param cls The class elegible for the clostest object. Use null to make all classes elegible.
     * @param me The calling Actor.
     * 
     * @return The closest Object of the class "cls" to the calling Actor "me".
     * 
     * TODO Get intet of Method
     */
    public Actor getClosestObject(Class cls, Actor caller) {
        // Retrieve all objects of the class "cls" currently present in the world and save them to a list.
        List<Actor> actors = getObjects(cls);
        // Remove the calling actor "me" from the list to avoid using distance to self.
        actors.remove(caller); 
        // Save the x and y coordinates of the calling Actor for computational efficiency.
        int x = caller.getX();
        int y = caller.getY();
        // Assume the first actor is the closest one till proofen otherwise.
        Actor closest = actors.get(0);
        double mindistance = getDistance(x, y, closest.getX(), closest.getY());
        double distance = 0;
        // Loop through all Actors in the list actors to find the closest actor of the class "cls".
        for (Actor a : actors) { 
            // Get the distance between the two Actors.
            distance = getDistance(x, y, a.getX(), a.getY()); 
            // Check if distance of the points closer than the previously chosen closest one.
            if (distance < mindistance) { 
                closest = a;
                mindistance = distance;
            }
        }
        return closest;
    }
    
    /**
     * Calculates the distance between two points using the Pythagorean equation
     * 
     * @param x1 The x coordinate of the first point.
     * @param y1 The y coordinate of the first point.
     * @param x2 The x coordinate of the second point.
     * @param y2 The y coordinate of the second point.
     * 
     * @return The distance of the first Point to the Second point as double.
     */
    private static double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }
    
    public static double getDistance(Actor a, Actor b) {
        return Math.sqrt(Math.pow(a.getX()-b.getX(),2)+Math.pow(a.getY()-b.getY(),2));
    }
    
    public void save(int slot) {
        FileWork.savePlayer(slot, this);
    }
    
    public static GreenfootImage scaleImage(GreenfootImage img, double scale) {
        img.scale((int) (img.getWidth()*scale*globalScale), (int) (img.getHeight()*scale*globalScale));
        return img;
    }
    
    public static int getRotationAngle(double[] dir) {
        double den = (Math.sqrt(Math.pow(dir[0], 2) + Math.pow(dir[1], 2)));
        double cos = dir[0]/den;
        int ret = (int) Math.round(Math.toDegrees(Math.acos(cos)));
        if (dir[1] < 0)
            return 360-ret;
        else
            return ret;
    }
}