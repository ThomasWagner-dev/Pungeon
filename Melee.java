 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for all Melee enemies. 
 * 
 * @author Commentator
 */
public class Melee extends Enemy
{
    
    public Melee(String name) {
        super(name);
    }
    
    /**
     * Act - do whatever the Zombie wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        super.act();
    }
    
    /**
     * defines standart movement for all melee enemies: Walk directly towards the player and ignore walls or others.
     */
    public double[][] getMovement() {
        DungeonWorld world = (DungeonWorld) getWorld();
        Player player = (Player) world.getClosestObject(Player.class, this);
        if (inRange(player)) return new double[][] {{0,0},{1}};
        return new double[][] {{player.getX()-getX(),player.getY()-getY()},{1}};
    }
    
    /**
     * returns a clone of this enemy
     */
    public Enemy clone() {
        return super.topClone(new Melee(name), this);
    }
}
