import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player.
 * 
 * @author Commentator
 */
public class Player extends Entity implements Collider{
    public Player() {
        speed = 5;
    }
    
    public void act() {
        super.act();
    }
    
    protected int[] getMovement() {
        return Inputs.getMovement();
    }
}
