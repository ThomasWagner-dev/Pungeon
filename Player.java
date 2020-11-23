package GreenfootGame;

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player.
 * 
 * @author Commentator
 */
public class Player extends Entity implements Collider{
    public Player() {
        speed = 5;
        maxhp = 20;
        hp = 20;
        dmgType = "physical";
        dmg = 7;
        type = "physical";
    }
    
    public void act() {
        super.act();
    }
    
    protected double[][] getMovement() {
        return Inputs.getMovement();
    }
}
