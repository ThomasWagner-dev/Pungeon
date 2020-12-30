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
        setSprite(sprite, 1);
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
}