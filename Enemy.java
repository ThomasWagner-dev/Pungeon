 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for all Enemies.
 * 
 * @author Commentator 
 */
public abstract class Enemy extends Entity implements Collider
{
    protected Weapon weapon;
    
    public void act() 
    {
        super.act();
        weapon.reduceCooldown();
        attack();
    }    
    
    /**
     * Abstract version of the attack method. Will be overwritten by all Subclasses, as all have a different attack scheme.
     */
    public void attack() {
        //System.out.println("attack!");
        DungeonWorld world = (DungeonWorld) getWorld();
        Player p = (Player) world.getClosestObject(Player.class, this); //fetches closest player
        double distance = DungeonWorld.getDistance(this, p); //gets the distance between oneself and the player
        //System.out.println(distance);//
        //checks if the distance to the player is close enough
        if (distance < weapon.range) {
            if (!checkCooldown()) return; //skips if there's still cooldown on the weapon
            world.addObject(new Projectile(new double[] {p.getX()-getX(),p.getY()-getY()}, weapon, this), getX(), getY()); //spawns attacking projectile
            weapon.resetCooldown(); //resets cooldown to max
        }
    }
    
    @Override
    public boolean checkCooldown() {
        return weapon.checkCooldown();
    }
}
