import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class Item extends Actor
{
    public final boolean instant;
    public final String changing, amount, name;
    
    public Item() {
        this("Item", false, "", "", "item/heart.png");
    }
    
    public Item(String name, boolean instant, String changing, String amount, String sprite) {
        this.name = name;
        this.instant = instant;
        this.changing = changing;
        this.amount = amount;
        setSprite(sprite, 0.5);
    }
    
    public Item(String name, boolean instant, String changing, String amount, GreenfootImage sprite) {
        this.name = name;
        this.instant = instant;
        this.changing = changing;
        this.amount = amount;
        setImage(sprite);
    }
    
    public void act() {
        if (isTouching(Player.class)) {
            collect();
        }
    }
    
    public void collect() {
        DungeonWorld world = (DungeonWorld) getWorld();
        world.musichandler.playSound("item", "collect");
        world.getObjects(Player.class).get(0).collect(this);
    }
    
    public void setSprite(String spriteName, double scale) {
        GreenfootImage tmp = new GreenfootImage(spriteName); //fetches image
        //tmp.scale((int) ((DungeonWorld.pixelSize*DungeonWorld.pixelSize*scale)/tmp.getWidth()),(int) ((DungeonWorld.pixelSize*DungeonWorld.pixelSize*scale)/tmp.getHeight())); //scales it to pixelsize times scale
        tmp.scale((int) (tmp.getWidth()*scale*DungeonWorld.globalScale), (int) (tmp.getHeight()*scale*DungeonWorld.globalScale));
        setImage(tmp);
    }
    
    public void drop(int x, int y, DungeonWorld world) {
        world.addObject(clone(), world.random.nextInt(world.pixelSize*2)-world.pixelSize+x, world.random.nextInt(world.pixelSize*2)-world.pixelSize+y);
        System.out.println("added item at: "+ x + " " + y);
    }
    
    public Item clone() {
        return new Item(name, instant, changing, amount, new GreenfootImage(getImage()));
    }
}