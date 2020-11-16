import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for all Enemies.
 * 
 * @author Commentator 
 */
public abstract class Enemy extends Entity
{
    public void act() 
    {
        super.act();
        attack();
    }    
    
    /**
     * Abstract version of the attack method. Will be overwritten by all Subclasses, as all have a different attack scheme.
     */
    public abstract void attack();
}
