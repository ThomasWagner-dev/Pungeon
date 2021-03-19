import java.util.*;

public class Loottable {
    public String name;
    public HashMap<String, Double> tableloot;
    public HashMap<String, Double> itemloot;

    /**
     * Creates a new loottable
     * @param name the name of the loottable
     * @param tableloot loot which are tables themselfes
     * @param itemloot loot which are items
     */
    public Loottable(String name, HashMap<String, Double> tableloot, HashMap<String, Double> itemloot) {
        this.name = name;
        this.tableloot = tableloot;
        this.itemloot = itemloot;
    }

    /**
     * drops a random item from the items in the table
     * @param world world the item is dropped into
     * @param x the x position where the item is dropped
     * @param y the y position the item is dropped
     */
    public void dropOne(DungeonWorld world, int x, int y) {
        double rnd,
                totalWeight = 0;
        for (String tablename : tableloot.keySet()) {
            totalWeight += tableloot.get(tablename);
        }

        for (String itemname : itemloot.keySet()) {
            totalWeight += itemloot.get(itemname);
        }
        rnd = world.random.nextDouble()*totalWeight;
        for (String tablename : tableloot.keySet()) {
            rnd -= tableloot.get(tablename);
            if (rnd <= 0) {
                world.loottables.get(tablename).dropOne(world, x, y);
                return;
            }
        }

        for (String itemname : itemloot.keySet()) {
            rnd -= itemloot.get(itemname);
            if (rnd <= 0) {
                System.out.println("dropping: " + itemname);
                world.items.get(itemname).clone().drop(x,y, world);
                return;
            }
        }
    }

    /**
     * selects a single, random item from a table
     * @param table the table the item is selected from
     * @param random the random number generator used.
     * @return the name of the item or table selected
     */
    public static String selectOne(HashMap<String, Double> table, Random random) {
        double totalWeight = 0,
            rnd;
        for (String key : table.keySet()) {
            totalWeight += table.get(key);
        }
        rnd = random.nextDouble()*totalWeight;
        for (String key : table.keySet()) {
            rnd -= table.get(key);
            if (rnd <= 0) return key;
        }
        System.err.println("in select one: random never reached 0");
        return "";
    }

    /**
     * selects a single, random item from a tag
     * @param tables the tables the item is selected from
     * @param random the random number generator used.
     * @return the name of the item or table selected
     */
    public static String selectOne(Tag[] tables, Random random) {
        HashMap<String, Double> t = new HashMap<>();
        for (Tag tag : tables) {
            if (tag.getName() == null) continue;
            t.put(tag.getName(), (Double) tag.getValue());
        }
        return selectOne(t, random);
    }

    public static void drop(Tag loottable, DungeonWorld world, int x, int y) {
        Tag slotTag = loottable.findNextTag("slots"),
                tables = loottable.findNextTag("table");
        double min = (Double) slotTag.get("min"),
                max = (Double) slotTag.get("max"),
                mean = (Double) slotTag.get("mean");

        int slotAmount = (int) world.nextGaussian(min, mean, max);
        System.out.println("loot has {} slots".replace("{}", slotAmount+""));
        for (int slot = 0; slot < slotAmount; slot++) {
            world.loottables.get(Loottable.selectOne((Tag[]) tables.getValue(), world.random)).dropOne(world, x, y);
        }
    }

    public static void drop(Loottable loottable, DungeonWorld world, int x , int y, int min, int max, int mean) {
        int slotAmount = (int) world.nextGaussian(min, mean, max);
        System.out.println("loot has {} slots".replace("{}", slotAmount+""));
        for (int slot = 0; slot < slotAmount; slot++) {
            loottable.dropOne(world, x, y);
        }
    }
}
