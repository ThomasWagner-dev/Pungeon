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

    /**
     * Checks if the actor is in attack range of this object
     * @param a actor to check range on
     * @return if the actor a is in range of this
     */
    public boolean inRange(Actor a) {
        double distance = DungeonWorld.getDistance(this, a); //fetch distance between me and actor
        int absRange = weapon.range * DungeonWorld.pixelSize + DungeonWorld.scaleImage(new GreenfootImage(weapon.hitbox), 1).getWidth() + DungeonWorld.pixelSize / 2; //calculate absolute range
        return distance <= absRange;
    }

    /**
     * Abstract version of the attack method.
     * Will be overwritten by all subclasses, as all have a different attack scheme.
     */
    public void attack() {
        //System.out.println("attack!");
        DungeonWorld world = (DungeonWorld) getWorld();
        // Get closest player.
        Player p = (Player) world.getClosestObject(Player.class, this);
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
        Tag loottable = world.mobdrops.findNextTag(name);
        double probability, drop;
        Item i;
        String name;
        if (loottable == null) {
            System.err.println("No loottable defined for {}".replace("{}", this.name));
        } else {
            Tag slotTag = loottable.findNextTag("slots"),
                tables = loottable.findNextTag("table");
            double min = (Double) slotTag.get("min"),
                    max = (Double) slotTag.get("max"),
                    mean = (Double) slotTag.get("mean");

            int slotAmount = (int) world.nextGaussian(min, mean, max),
                x = getX(),
                y = getY();
            System.out.println("loot has {} slots".replace("{}", slotAmount+""));
            for (int slot = 0; slot < slotAmount; slot++) {
                world.loottables.get(Loottable.selectOne((Tag[]) tables.getValue(), world.random)).dropOne(world, x, y);
            }
        }
        super.die();
    }
}