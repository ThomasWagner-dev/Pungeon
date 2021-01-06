import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * The Superclass for all Enemies.
 */
public abstract class Enemy extends Entity implements Cloneable {
    protected Weapon weapon;
    //public final String name;

    public Enemy(String name) {
        this.name = name;
    }

    public void act() {
        super.act();
        weapon.reduceCooldown();
        attack();
    }

    public boolean inRange(Actor a) {
        double distance = DungeonWorld.getDistance(this, a); //fetch distance between me and actor
        int absRange = weapon.range * DungeonWorld.pixelSize + DungeonWorld.scaleImage(new GreenfootImage(weapon.hitbox), 1).getWidth() + DungeonWorld.pixelSize / 2; //calculate absolute range
        return distance <= absRange;
    }

    /**
     * TODO
     * Abstract version of the attack method.
     * Will be overwritten by all subclasses, as all have a different attack scheme.
     */
    public void attack() {
        //System.out.println("attack!");
        DungeonWorld world = (DungeonWorld) getWorld();
        // Get closest player.
        Player p = (Player) world.getClosestObject(Player.class, this);
        //System.out.println(distance);//
        //checks if the distance to the player is close enough
        //System.out.println((new GreenfootImage(weapon.hitbox)).getWidth()+DungeonWorld.pixelSize/2);
        //System.out.println();
        // Spawn attack if the player is close enough.
        if (inRange(p)) {
            weapon.attack(world, this, new double[]{p.getX() - getX(), p.getY() - getY()});
        }
    }

    /**
     * Abstract clone method.
     * Will be overwritten by all subclasses.
     */
    public abstract Enemy clone();

    /**
     * Clones the parameters of the enemy origin to the enemy e.
     * e has to be provided as Enemy itself is abstract
     * TODO
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

    /**
     * Removes self from the world and drop the specified Loot.
     */
    public void die() {
        DungeonWorld world = (DungeonWorld) getWorld();
        Random random = world.random;
        Tag loottable = world.loottables.findNextTag(name);
        double probability, drop;
        Item i;
        if (loottable == null) {
            System.err.println("No loottable defined for {}".replace("{}", name));
        } else {
            for (Tag t : (Tag[]) loottable.getValue()) {
                i = world.items.get(t.getName());
                if (i == null) continue;
                probability = (double) t.getValue();
                drop = random.nextDouble();
                System.out.println("drop: " + drop + " prob: " + probability);
                if (probability > random.nextDouble()) {
                    i.drop(getX(), getY(), world);
                }
                System.out.println(t.getName());
            }
        }
        super.die();
    }
}