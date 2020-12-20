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
    public final String dmgType, trigger;
    private int currentCooldown;
    public final String inactiveImg, activeImg;
    
    public Trap(int range, String inactiveImg, String activeImg, int dmg, String dmgType, String trigger, int cooldown) {
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.cooldown = cooldown;
        this.range = range;
        currentCooldown = 0;
        this.trigger = trigger;
        this.inactiveImg = inactiveImg;
        this.activeImg = activeImg;
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
        return topCloning(new Trap(range, inactiveImg, activeImg, dmg, dmgType, trigger, cooldown));
    }
}
