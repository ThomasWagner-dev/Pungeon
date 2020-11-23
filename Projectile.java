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
    protected int lifespan;
    protected Actor me;
    // public Projectile(int[] direction, int dmg, String dmgType) {
        // new Projectile(direction, dmg, dmgType, false);
    // }
    
    /**
     * moves and then checks for all collisions. See collide(Projectile p) in Entity for further detail
     */
    public void act() {
        super.act();
        lifespan--;
        
        if (lifespan == 0) {
            getWorld().removeObject(this);
        }
        
        for (Wall w : getIntersectingObjects(Wall.class)) {
            w.collide(this);
        }
        
        for (Entity e : getIntersectingObjects(Entity.class)) {
            e.collide(this);
        }
    }
    
    public Projectile(double[] direction, int dmg, String dmgType, boolean isReflective, String spriteName, int lifespan, Actor me) {
        this.direction = direction; 
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
        this.lifespan = lifespan;
        this.me = me;
        
        setImage(spriteName);
    }
    
    public Projectile(double[] direction, int dmg, String dmgType, boolean isReflective, String spriteName, Actor me) {
        this.direction = direction; 
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
        this.lifespan = -1;
        this.me = me;
        
        setImage(spriteName);
    }
    
    
    protected double[][] getMovement() {
        System.out.println("test");
        return new double[][] {direction,{1}};
    }
}
