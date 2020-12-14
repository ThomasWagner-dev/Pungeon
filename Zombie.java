 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Zombie here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Zombie extends Melee //implements Collider
{
    /**
     * creates a basic melee zombie
     */
    public Zombie() {
        //super();
        weapon = DungeonWorld.weapons.get("claw_rat");
        speed = 2;
        attackcooldown = 100;
        dmg = 5;
        dmgType = "physical";
        type = "physical";
        maxhp = 20;
        hp = 20;
    }
    
    
}
