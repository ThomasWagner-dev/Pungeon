import java.util.*;
import greenfoot.*;
/**
 * 
 * 
 * @author Commentator
 * @version 2020-12-24-13-03
 */
public class Screen  
{
    public final ArrayList<ArrayList<Block>> map;
    public final HashMap<Enemy, int[]> enemies;
    public final DungeonWorld world;
    public HashMap<String, String> adjacentScreens;
    public Block backgroundBlock;
    
    public Screen(ArrayList<ArrayList<String>> rawMap, HashMap<Enemy, int[]> enemies, Block background, HashMap<String, Block> blocks, HashMap<String, String> adjacentScreens, DungeonWorld world) {
        map = loadMap(rawMap, blocks);
        this.enemies = enemies;
        this.world = world;
        this.adjacentScreens = adjacentScreens;
        backgroundBlock = background;
    }
    
    public static ArrayList<ArrayList<Block>> loadMap(ArrayList<ArrayList<String>> rawMap, HashMap<String, Block> blocks) {
        ArrayList<ArrayList<Block>> map = new ArrayList<>();
        ArrayList<Block> tmp;
        for (ArrayList<String> ttmp : rawMap) {
            tmp = new ArrayList<>();
            for (String b : ttmp) {
                tmp.add(blocks.get(b).clone());
            }
            map.add(tmp);
        }
        return map;
    }
    
    public void load() {
        List<Actor> actress = world.getObjects(null);
        actress.remove(world.getObjects(Player.class));
        world.removeObjects(actress);
        
        // Render the world.
        int row = 0, x, y;
        // Load and add all Blocks to the world.
        while (row < map.size()) {
            y = row * DungeonWorld.pixelSize + DungeonWorld.pixelSize/2;
            for (int i = 0; i < map.get(row).size(); i++) {
                x = i * DungeonWorld.pixelSize + DungeonWorld.pixelSize/2;
                world.addObject(
                        map.get(row).get(i),
                        x,
                        y
                );
                if (map.get(row).get(i) instanceof Wall) {
                    world.addObject(backgroundBlock, x, y);
                }
            }
            row++;
        }
    }
}
