package GreenfootGame;

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player.
 * 
 * @author Commentator
 */
public class Player extends Entity implements Collider{
    protected double[] rotation;
    protected Weapon selectedWeapon;
    
    public Player() {
        speed = 5;
        maxhp = 20;
        hp = 20;
        dmgType = "physical";
        dmg = 7;
        type = "physical";
        rotation = new double[] {0,0};
        selectedWeapon = new Weapon("Sword", "*poke*", 30, dmg, type, "apple1.png");
    }
    
    public void act() {
        super.act();
    }
    
    public void attack() {
        Projectile p = 
            new Projectile(
                rotation, 
                selectedWeapon.dmg, 
                selectedWeapon.dmgType, 
                false, 
                selectedWeapon.spriteName, 
                selectedWeapon.range, 
                this
            );
        getWorld().addObject(p, getX(), getY());
    }
    
    protected double[][] getMovement() {
        double[][] movement = Inputs.getMovement();
        rotation = movement[0];
        return movement;
    }
}
