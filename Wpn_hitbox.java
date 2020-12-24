import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;


public class Wpn_hitbox extends Projectile
{
    List<Entity> collided = new ArrayList<>();
    /**
     * Act - do whatever the Wpn_hitbox wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        lifespan--;
        List<Entity> intersectingEntities = getIntersectingObjects(Entity.class);
        intersectingEntities.remove(this);
        intersectingEntities.remove(me);
        intersectingEntities.removeAll(collided);
        collided.addAll(intersectingEntities);
        for (Entity e : intersectingEntities) {
            System.out.println(e);
            e.collide(this);
        }
        if (lifespan <= 0) 
            getWorld().removeObject(this);
    } 
    
    public Wpn_hitbox(double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, Actor me) {
        super(direction, dmg, dmgType, speed, isReflective, spriteName, me);
        lifespan=5;
    }
    
    public Wpn_hitbox(double[] direction, Weapon selectedWeapon, Actor root) {
        super(direction, selectedWeapon, root);
    }
}
