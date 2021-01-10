import greenfoot.*;

/**
 * Weapons. They attack.
 *
 * @author Commentator
 */
public class Weapon extends Item{
    public final int range, dmg, maxCooldown, speed;
    public final double scale;
    public final String name, description, dmgType, displayName;
    public final String spriteName, hitbox;
    public final GreenfootImage img;
    public int cooldown, angle = 0;
    public final boolean isMelee;

    public Weapon(String name, String displayname, String description, int range, int dmg, String dmgType, int speed, int cooldown, String spriteName, String hitbox, boolean isMelee) {
        this(name, displayname, description, range, dmg, dmgType, speed, cooldown, spriteName, 1, hitbox, isMelee);
    }

    public Weapon(String name, String displayname, String description, int range, int dmg, String dmgType, int speed, int cooldown, String spriteName, double scale, String hitbox, boolean isMelee) {
        this(name, displayname, description, range, dmg, dmgType, speed, cooldown, spriteName, scale, hitbox, isMelee, 0);
    }

    /**
     * generates a new weapon
     * @param name allocation name of the weapon
     * @param displayname displayname of the weapon
     * @param description weapons description
     * @param range range of the weapon in blocks
     * @param dmg the amount of damage the weapon deals
     * @param dmgType the type of damage (e.g. physical or fire) the weapon deals
     * @param speed the speed teh projectile moves with
     * @param cooldown the cooldown or time one has to wait before being able to use the weapon again
     * @param spriteName name of teh sprite the weapon has
     * @param scale the scale the weapon gets displayed with
     * @param hitbox the hitbox of the weapon (or projectile)
     * @param isMelee if teh weapon is a melee weapon (is this unnessesary by now?)
     * @param angle the standard rotation angle of the projectile when spawned
     */
    public Weapon(String name, String displayname, String description, int range, int dmg, String dmgType, int speed, int cooldown, String spriteName, double scale, String hitbox, boolean isMelee, int angle) {
        super(name, false, null, null, spriteName);
        this.range = range;
        this.name = name;
        this.description = description;
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.spriteName = spriteName;
        this.cooldown = 0;
        this.maxCooldown = cooldown;
        this.speed = speed;
        this.hitbox = hitbox;
        this.scale = scale;
        this.displayName = displayname;
        this.isMelee = isMelee;
        this.angle = angle;
        img = DungeonWorld.scaleImage(new GreenfootImage(spriteName), scale);
    }

    /**
     * reduces the weapons cooldown
     */
    public void reduceCooldown() {
        cooldown--;
    }

    /**
     * returns an exact clone of the weapon
     * @return a brand new Clone (wars)
     */
    public Weapon clone() {
        return new Weapon(name, displayName, description, range, dmg, dmgType, speed, maxCooldown, spriteName, scale, hitbox, isMelee, angle);
    }

    /**
     * resets the cooldown to the maximum
     */
    public void resetCooldown() {
        cooldown = maxCooldown;
    }

    /**
     * checks if one is able to use the weapon
     * @return if one is able to use the weapon
     */
    public boolean checkCooldown() {
        return cooldown <= 0;
    }

    /**
     * called when a player or enemy uses the weapon
     * @param world the world the projectile gets placed in
     * @param owner the owner of the weapon (so they don't get hurt by it)
     * @param direction direction the projectile is flying in
     */
    public void attack(DungeonWorld world, Actor owner, double[] direction) {
        if (!checkCooldown()) return;
        if (owner instanceof Enemy) {
            Enemy e = (Enemy) owner;
            int cap = 0;
            if (e instanceof Collider) cap = 1;
            if (e.getObjectAtOffset(0, 0, Collider.class).size() != cap) return;
        }
        resetCooldown();
        Projectile p;
        //System.out.println(isMelee);
        if (!isMelee) {
            p = new Projectile(
                    direction,
                    this,
                    owner
            );
        } else {
            p = new Wpn_hitbox(direction, this, owner);
        }
        p.setRotation(DungeonWorld.getRotationAngle(direction) + angle);
        world.addObject(p, owner.getX(), owner.getY());
        p.move(DungeonWorld.pixelSize / 2);
        world.musichandler.playSound("wpn", name);
    }
}
