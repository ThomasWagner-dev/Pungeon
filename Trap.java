import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * 
 * 
 * @author Commentator 
 * @version 2020-11-24-1207
 */
public class Trap extends Block
{
    public final int dmg, cooldown, range;
    public final String dmgType;
    private int currentCooldown;
    
    public Trap(int dmg, String dmgType, int cooldown, int range) {
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.cooldown = cooldown;
        this.range = range;
        currentCooldown = 0;
    }
    
    
    public void act() 
    {
        currentCooldown--;
        if (currentCooldown > 0) return;
        
        List<Entity> attackingObjects = getObjectsInRange(range, Entity.class);
        if(attackingObjects.size() > 0) {
            attackingObjects.forEach(e -> e.takeDamage(this));
            //TODO: play animation
            currentCooldown = cooldown;
        }
    }    
    
    public Block clone() {
        return new Trap(dmg, dmgType, cooldown, range);
    }
}
