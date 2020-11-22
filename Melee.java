import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for all Melee enemies. 
 * 
 * @author Commentator
 */
public abstract class Melee extends Enemy
{
    /**
     * Act - do whatever the Zombie wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        super.act();
    }   
    
    /**
     * modified attack method.
     */
    public void attack() {
        DungeonWorld world = (DungeonWorld) getWorld();
        if (isTouching(Player.class)) {
            if (!checkCooldown())
                return;
            Player player = (Player) world.getClosestObject(Player.class, this);
            player.takeDamage(this);
        }
    }
    
    /**
     * defines standart movement for all melee enemies: Walk directly towards the player and ignore walls or others.
     */
    public double[][] getMovement() {
        DungeonWorld world = (DungeonWorld) getWorld();
        Player player = (Player) world.getClosestObject(Player.class, this);
        return new double[][] {{player.getX()-getX(),player.getY()-getY()},{1}};
    }
}
