import java.util.*;

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
    //protected final int dmg;
    //protected final String dmgType;
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
            return;
        }
        List<Wall> intersectingWalls = getIntersectingObjects(Wall.class);
        List<Entity> intersectingEntities = getIntersectingObjects(Entity.class);
        intersectingWalls.remove(this);
        intersectingEntities.remove(this);
        intersectingEntities.remove(me);
        System.out.println(intersectingEntities);
        for (Wall w : intersectingWalls) {
            w.collide(this);
        }
        
        for (Entity e : intersectingEntities) {
            e.collide(this);
        }
    }
    
    public Projectile(double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, int lifespan, Actor me) {
        this.direction = direction; 
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
        this.lifespan = lifespan;
        this.me = me;
        this.speed = speed;
        
        setImage(spriteName);
    }
    
    public Projectile(double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, Actor me) {
        this.direction = direction; 
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
        this.lifespan = -1;
        this.me = me;
        this.speed = speed;
        
        setImage(spriteName);
    }
    
    
    protected double[][] getMovement() {
        System.out.println(Arrays.toString(direction));
        return new double[][] {direction,{1}};
    }
}
