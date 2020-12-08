 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player.
 * 
 * @author Commentator
 */
public class Player extends Entity implements Collider{
    protected double[] rotation;
    protected Weapon selectedWeapon;
    protected Inputs inputs = new Inputs(new KeyLayout());
    
    public Player() {
        speed = 5;
        maxhp = 20;
        hp = 20;
        dmgType = "physical";
        dmg = 7;
        type = "physical";
        rotation = new double[] {0,0};
        selectedWeapon = new Weapon("Sword", "*poke*", 3, dmg, type, 15, "apple1.png", 100);
    }
    
    public void act() {
        super.act();
        selectedWeapon.cooldown--;
        if (selectedWeapon.cooldown <= 0 && inputs.attacks()) {
            attack();
        }
    }
    
    public void attack() {
        selectedWeapon.cooldown = selectedWeapon.maxCooldown;
        Projectile p = 
            new Projectile(
                rotation, 
                selectedWeapon.dmg, 
                selectedWeapon.dmgType,
                selectedWeapon.speed,
                false, 
                selectedWeapon.spriteName, 
                selectedWeapon.range, 
                this
            );
        getWorld().addObject(p, getX(), getY());
    }
    
    protected double[][] getMovement() {
        double[][] movement = inputs.getMovement();
        if (movement[0][0] != 0 || movement[0][1] != 0)
            rotation = movement[0];
        return movement;
    }
}
