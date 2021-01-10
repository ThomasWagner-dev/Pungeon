

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The super class for all Blocks.
 */
public abstract class Block extends Actor implements Cloneable {
    /**
     * A public version of the protected clone() method of Object. Allows blocks to be cloned, making loading easier.
     */
    public abstract Block clone();

    protected Block topCloning(Block b) {
        b.setImage(getImage());
        return b;
    }
}
