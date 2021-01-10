

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for all colliding Melee enemies.
 */
public class CollidingEnemy extends Enemy implements Collider {

    public CollidingEnemy(String name) {
        super(name);
    }

    public void act() {
        super.act();
    }

    /**
     * Defines the standart movement for all melee enemies:
     * Walk directly towards the player and ignore walls or others.
     */
    public double[][] getMovement() {
        DungeonWorld world = (DungeonWorld) getWorld();
        Player player = (Player) world.getClosestObject(Player.class, this);
        if (inRange(player)) return new double[][]{{0, 0}, {1}};
        return new double[][]{{player.getX() - getX(), player.getY() - getY()}, {1}};
    }

    /**
     * Returns a clone of this enemy.
     */
    public Enemy clone() {
        return super.topClone(new CollidingEnemy(name), this);
    }
}
