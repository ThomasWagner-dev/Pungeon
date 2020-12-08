 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A basic Tile which can trigger interactions.
 * 
 * @author (Screxo) 
 */
public class Tile extends Block
{
    public void act() 
    {
        
    }    
    
    public Block clone() {
        return topCloning(new Tile());
    }
}
