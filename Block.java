 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Block here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public abstract class Block extends Actor
{
    /**
     * A public version of the protected clone() method of Object. Allows blocks to be cloned, to make map-loading easier.
     */
    public abstract Block clone();
    
    protected Block topCloning(Block b) {
        b.setImage(getImage());
        return b;
    }
}
