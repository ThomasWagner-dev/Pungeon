public class Item extends WorldObj implements Cloneable {
    public final boolean instant;
    public final String changing, amount, name;

    /**
     * creates an empty item
     */
    public Item() {
        this("Item", false, "", "", "item/heart.png");
    }

    /**
     * creates a new Item
     * @param name name of the item
     * @param instant if the item is used instantly or put into the inventory
     * @param changing which value the item changes if it is instant
     * @param amount the amount the item changes if instant
     * @param sprite the sprite of the item
     */
    public Item(String name, boolean instant, String changing, String amount, String sprite) {
        this.name = name;
        this.instant = instant;
        this.changing = changing;
        this.amount = amount;
        setSprite(sprite, 0.5);
    }

    /**
     * creates a new Item
     * @param name name of the item
     * @param instant if the item ist used instantly or put into the inventor
     * @param changing which value the item changes if it is instant
     * @param amount the amount the item changes if instant
     * @param sprite the sprite of the item
     */
    public Item(String name, boolean instant, String changing, String amount, AdvancedImage sprite) {
        this.name = name;
        this.instant = instant;
        this.changing = changing;
        this.amount = amount;
        setImage(sprite);
    }

    public void tick() {
        if (isTouching(Player.class)) {
            collect();
        }
    }

    /**
     * is executed when the item is collected by a player
     */
    public void collect() {
        DungeonWorld world = (DungeonWorld) this.world;
        world.musichandler.playSound("item", "collect");
        world.objectsOf(Player.class).get(0).collect(this);
    }

    /**
     * sets the spriet to the spritename and scale
     * {@see Entity#setSprite} for more information
     * @param spriteName name/location of the sprite
     * @param scale scale the sprite will be scaled to
     */
    public void setSprite(String spriteName, double scale) {
        AdvancedImage tmp = Utils.loadImageFromAssets(spriteName); //fetches image
        //tmp.scale((int) ((DungeonWorld.pixelSize*DungeonWorld.pixelSize*scale)/tmp.getWidth()),(int) ((DungeonWorld.pixelSize*DungeonWorld.pixelSize*scale)/tmp.getHeight())); //scales it to pixelsize times scale
        tmp = tmp.scale((int) (tmp.getWidth() * scale * DungeonWorld.globalScale), (int) (tmp.getHeight() * scale * DungeonWorld.globalScale));
        setImage(tmp);
    }

    /**
     * executed when something drops this item
     * @param x x position of the drop
     * @param y y position of the drop
     * @param world the world the items gets spawned in
     */
    public void drop(int x, int y, DungeonWorld world) {
        int sx, sy;
        do {
            sy = world.random.nextInt(world.pixelSize * 2) - world.pixelSize + y;
            sx = world.random.nextInt(world.pixelSize*2)- world.pixelSize +x;
        } while(world.objectsOfInterfaceOn(sx, sy, Collider.class).size() != 0);
        world.addObject(clone(), sx, sy);
        System.out.println("added item at: " + x + " " + y);
    }

    /**
     * clones this item
     * @return a new Item and an extick clone of this
     */
    public Item clone() {
        return new Item(name, instant, changing, amount, new AdvancedImage(img));
    }
}