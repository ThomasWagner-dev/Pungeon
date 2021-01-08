import java.util.*;

import greenfoot.*;

/**
 * @author Commentator
 * @version 2021-01-01-22-37
 */
public class Screen {
    public final ArrayList<ArrayList<Block>> map;
    public final HashMap<Enemy, int[]> enemies;
    public HashMap<String, String> adjacentScreens;
    public Block backgroundBlock;
    public final String name;

    public Screen(String name, ArrayList<ArrayList<String>> rawMap, HashMap<Enemy, int[]> enemies, Block background, HashMap<String, Block> blocks, HashMap<String, String> adjacentScreens, ImageGenerator imgGen, DungeonWorld world) {
        this.name = name;
        map = loadMap(rawMap, blocks, imgGen, world);
        this.enemies = enemies;
        this.adjacentScreens = adjacentScreens;
        backgroundBlock = background;
    }

    public static ArrayList<ArrayList<Block>> loadMapOld(ArrayList<ArrayList<String>> rawMap, HashMap<String, Block> blocks, DungeonWorld world) {
        ArrayList<ArrayList<Block>> map = new ArrayList<>();
        ArrayList<Block> tmp;
        Block tmpBlock;
        GreenfootImage img;
        for (ArrayList<String> ttmp : rawMap) {
            tmp = new ArrayList<>();
            for (String b : ttmp) {
                try {
                    tmpBlock = blocks.get(world.applyGenerationNoise(b)).clone();
                    img = tmpBlock.getImage();
                    img.scale(64, 64);
                    tmpBlock.setImage(img);
                    tmp.add(blocks.get(b).clone());
                } catch (Exception e) {
                    System.err.println("Error while cloning block {}".replace("{}", b));
                    e.printStackTrace();
                    throw e;
                }
            }
            map.add(tmp);
        }
        return map;
    }

    public static ArrayList<ArrayList<Block>> loadMap(ArrayList<ArrayList<String>> rawMap, HashMap<String, Block> blocks, ImageGenerator imgGen, DungeonWorld world) {
        ArrayList<ArrayList<Block>> map = new ArrayList<>();
        ArrayList<Block> tmp;
        Block blk;
        GreenfootImage img;
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
                        img = blk.getImage();
                    }
                    img.scale(DungeonWorld.pixelSize, DungeonWorld.pixelSize);
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

    public void load(DungeonWorld world) {
        List<Actor> actress = world.getObjectsExclusive(Player.class);
        //List<Actor> actress = world.getObjects(null);
        actress.removeAll(world.getObjects(Counter.class));
        world.removeObjects(actress);
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
            pos = enemies.get(e);
            pos = DungeonWorld.compilePosition(pos);
            world.addObject(
                    e.clone(),
                    pos[0],
                    pos[1]
            );
        }
        world.activeScreen = this;
    }
}
