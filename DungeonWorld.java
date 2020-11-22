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
    protected final HashMap<String, Block> blocks;
    
    /**
     * TODO: make loading async
     */
    public DungeonWorld()
    {    
        // Create a new world with 1425x850 cells with a cell size of 1x1 pixels.
        super(1425, 850, 1);
        //Load dmgMultipliers.
        System.out.println("Start loading...");
        
        System.out.println("Loading dmgMultipliers");
        dmgMultiplier = FileWork.getDmgMultiplier();
        
        System.out.println("Loading Blocks");
        blocks = FileWork.loadAllBlocks();
        
        
        System.out.println("Finished loading.");
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
        String[][] world = FileWork.loadWorldFile(screenName);
        //Render the world.
        int row = 0;
        //Load and add all Actors to the world.
        while (row < world.length) {
            for (String block : world[row]) {
                addObject(blocks.get(block).clone(), 0, 0);
                //addObject((Actor)FileWork.loadBlock(block), 0, 0);// TODO think about a tile and wall size.
            }
            row++;
        }
        removeObject(loadingScreen);
        setPaintOrder(Player.class, Enemy.class, Projectile.class);//TODO Update paintorder after being done.
    }
    
    /**
     * Gets the closest Object of a specific class to the actor me.
     * 
     * @param cls the Class which the closest Object is type of. use null to get an object of any class present.
     * @param me the Actor, whose closest Object should be returned
     * @return the object with the closest distance to the actor.
     */
    public Actor getClosestObject(Class cls, Actor me) {
        List<Actor> actors = getObjects(cls); //retrieves a list of objects of the class cls currently present in the world.
        actors.remove(me); //removes me (the calling actor) to not get itself.
        int x = me.getX(), y = me.getY(); //sets x and y coordinates to not retrieve them every single time
        double distance, mindistance = -1; 
        Actor closest = null;
        for (Actor a : actors) { //loops thoughout all present Actors to see which one is closest
            distance = getDistance(x, y, a.getX(), a.getY()); //retrieves the distance between the two points.
            if (distance < mindistance || mindistance == -1) { //checks if distance is closer than the previously chosen closest or if the distance is -1 which is the initvalue.
                closest = a;
                mindistance = distance;
            }
        }
        return closest;
    }
    
    /**
     * returns the distance using Pytaguras
     */
    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2-x1,2)+Math.pow(y2-y1,2));
    }
}