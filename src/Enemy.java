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

    public void tick() {
        super.tick();
        weapon.reduceCooldown();
        attack();
    }

    /**
     * Checks if the WorldObj is in attack range of this object
     * @param a WorldObj to check range on
     * @return if the WorldObj a is in range of this
     */
    public boolean inRange(WorldObj a) {
        double distance = DungeonWorld.getDistance(this, a); //fetch distance between me and WorldObj
        int absRange = weapon.range * DungeonWorld.pixelSize + DungeonWorld.scaleImage(Utils.loadImageFromAssets(weapon.hitbox), 1).getWidth() + DungeonWorld.pixelSize / 2; //calculate absolute range
        return distance <= absRange;
    }

    /**
     * Abstrtick version of the attack method.
     * Will be overwritten by all subclasses, as all have a different attack scheme.
     */
    public void attack() {
        //System.out.println("attack!");
        DungeonWorld world = (DungeonWorld) this.world;
        // Get closest player.
        Player p = (Player) world.getClosestObject(Player.class, this);
        // Spawn attack if the player is close enough.
        if (inRange(p)) {
            weapon.attack(world, this, new double[]{p.x - x, p.y - y});
        }
    }

    /**
     * Abstrtick clone method.
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
        e.tickiveEffects = origin.tickiveEffects;
        e.setImage(origin.img);
        e.weapon = origin.weapon;
        return e;
    }

    /**
     * Removes self from the world and drop the specified Loot.
     */
    public void die() {
        DungeonWorld world = (DungeonWorld) this.world;
        System.out.println(name);
        Tag loottable = world.mobdrops.findNextTag(name);
        if (loottable == null) {
            System.err.println("No loottable defined for {}".replace("{}", this.name));
        } else {
            Loottable.drop(loottable, world, x, y);
        }
        super.die();
    }
}