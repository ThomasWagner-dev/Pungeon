import java.util.*;
/**
 * @author Commentator
 * @version 2021-01-01-22-37
 */
public class Screen {
    public ArrayList<ArrayList<Block>> map;
    public HashMap<Enemy, int[]> enemies;
    public HashMap<String, String> adjacentScreens;
    public ArrayList<WorldObj> additionalObjs = new ArrayList<>();
    public Block backgroundBlock;
    public String name;

    /**
     * creates a new screen.
     * @param name name of the screen
     * @param rawMap the raw map of blocks (as strings)
     * @param enemies all enemies present on this screen
     * @param background the background tile, if any blocks get destroyed
     * @param blocks a list of all blocks available to load
     * @param adjacentScreens a map of adjacent screens
     * @param imgGen an image generator for connected textures
     * @param world the world for generation noise purposes
     */
    public Screen(String name, ArrayList<ArrayList<String>> rawMap, HashMap<Enemy, int[]> enemies, Block background, HashMap<String, Block> blocks, HashMap<String, String> adjacentScreens, ImageGenerator imgGen, DungeonWorld world) {
        this.name = name;
        map = loadMap(rawMap, blocks, imgGen, world);
        this.enemies = enemies;
        this.adjacentScreens = adjacentScreens;
        backgroundBlock = background;
    }

    /**
     * compiles a raw map into a map
     * @param rawMap map of blocks as strings
     * @param blocks all blocks available to load
     * @param imgGen image generator for connected textures
     * @param world the world for generation noise purposes
     * @return returns a map of blocks
     */
    public static ArrayList<ArrayList<Block>> loadMap(ArrayList<ArrayList<String>> rawMap, HashMap<String, Block> blocks, ImageGenerator imgGen, DungeonWorld world) {
        ArrayList<ArrayList<Block>> map = new ArrayList<>();
        ArrayList<Block> tmp;
        Block blk;
        AdvancedImage img;
        String blkname;
        for (int y = 0; y < rawMap.size(); y++) {
            tmp = new ArrayList<>();
            for (int x = 0; x < rawMap.get(0).size(); x++) {
                try {
                    blkname = world.applyGenerationNoise(rawMap.get(y).get(x));
                    rawMap.get(y).set(x, blkname);
                    blk = blocks.get(blkname).clone();
                    img = imgGen.generateWallImage(x, y, rawMap);
                    if (img == null) {
                        img = blk.img;
                    }
                    img = img.scale(DungeonWorld.pixelSize, DungeonWorld.pixelSize);
                    blk.setImage(img);
                    tmp.add(blk);
                } catch (Exception e) {
                    System.err.println("Error while cloning block {}".replace("{}", rawMap.get(y).get(x)));
                    throw e;
                }
            }
            map.add(tmp);
        }
        return map;
    }

    /**
     * loads the screen onto the world
     * @param world world the screen gets generated on
     */
    public void load(DungeonWorld world) {
        world.removeObjectsExclusive(Player.class, Counter.class);
        // Render the world.
        int row = 0, x, y;
        // Load and add all Blocks to the world.
        while (row < map.size()) {
            y = row * DungeonWorld.pixelSize + DungeonWorld.pixelSize / 2;
            for (int i = 0; i < map.get(row).size(); i++) {
                x = i * DungeonWorld.pixelSize + DungeonWorld.pixelSize / 2;
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

        //load Enemies
        int[] pos;
        for (Enemy e : enemies.keySet()) {
            if (e.hp <= 0) continue;
            pos = enemies.get(e);
            pos = DungeonWorld.compilePosition(pos);
            world.addObject(
                    e,//.clone(),
                    pos[0],
                    pos[1]
            );
            if (e.weapon != null)
                e.weapon.cooldown = 50;
        }

        additionalObjs.forEach(obj -> world.addObject(obj, obj.x, obj.y));
        world.tickiveScreen = this;
    }
}
