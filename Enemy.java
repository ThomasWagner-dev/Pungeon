 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for all Enemies.
 * 
 * @author Commentator 
 */
public abstract class Enemy extends Entity implements Collider
{
    protected Weapon weapon;
    //public final String name;
    
    public Enemy(String name) {
        this.name = name;
    }
    
    public void act() 
    {
        super.act();
        weapon.reduceCooldown();
        attack();
    }    
    
    /**
     * Abstract version of the attack method. Will be overwritten by all Subclasses, as all have a different attack scheme.
     */
    public void attack() {
        //System.out.println("attack!");
        DungeonWorld world = (DungeonWorld) getWorld();
        Player p = (Player) world.getClosestObject(Player.class, this); //fetches closest player
        //System.out.println(distance);//
        //checks if the distance to the player is close enough
        //System.out.println((new GreenfootImage(weapon.hitbox)).getWidth()+DungeonWorld.pixelSize/2);
        //System.out.println();
        if (inRange(p)) {
            weapon.attack(world, this, new double[] {p.getX()-getX(), p.getY()-getY()}); //spawns attacking projectile
        }
    }
     
    /**
     * Clones the parameters of @param[origin] to @param[e]. e has to be provided as Enemy itself is abstract
     */
    public static Enemy topClone(Enemy e, Enemy origin) {
        e.hp = origin.hp;
        e.maxhp = origin.maxhp;
        e.dmg = origin.dmg;
        e.speed = origin.speed;
        e.type = origin.type;
        e.activeEffects = origin.activeEffects;
        e.setImage(origin.getImage());
        e.weapon = origin.weapon;
        return e;
    }
    
    public Enemy clone() {System.out.println("hallo ich sollte nicht aufgerufen werden");return null;}
    
    public boolean inRange(Actor a) {
        double distance = DungeonWorld.getDistance(this, a); //fetch distance between me and actor
        int absRange = weapon.range * DungeonWorld.pixelSize + DungeonWorld.scaleImage(new GreenfootImage(weapon.hitbox),1).getWidth() + DungeonWorld.pixelSize/2; //calculate absolute range
        return distance <= absRange;
    }
}
