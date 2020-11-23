package GreenfootGame;

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Projectiles. Flying objects which move through space and deal damage.
 * 
 * @author Commentator
 */
public class Projectile extends Entity
{
    public double[] direction;
    public final boolean isReflective;
    protected final int dmg;
    protected final String dmgType;
    
    // public Projectile(int[] direction, int dmg, String dmgType) {
        // new Projectile(direction, dmg, dmgType, false);
    // }
    
    /**
     * moves and then checks for all collisions. See collide(Projectile p) in Entity for further detail
     */
    public void act() {
        super.act();
        System.out.println("text");
        for (Wall w : getIntersectingObjects(Wall.class)) {
            w.collide(this);
        }
        
        for (Entity e : getIntersectingObjects(Entity.class)) {
            e.collide(this);
        }
    }
    
    public Projectile(double[] direction, int dmg, String dmgType, boolean isReflective) {
        this.direction = direction; 
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
    }
    
    
    protected double[][] getMovement() {
        System.out.println("test");
        return new double[][] {direction,{1}};
    }
}
