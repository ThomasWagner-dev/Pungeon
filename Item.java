import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public abstract class Item extends Actor
{
    public void collect() {
        DungeonWorld world = (DungeonWorld) getWorld();
        world.musichandler.playSound("item", "collect");
    }
}