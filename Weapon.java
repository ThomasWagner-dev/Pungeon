import greenfoot.*;

/**
 * Weapons. They attack.
 *
 * @author Commentator
 */
public class Weapon {
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

    public Weapon(String name, String displayname, String description, int range, int dmg, String dmgType, int speed, int cooldown, String spriteName, double scale, String hitbox, boolean isMelee, int angle) {
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

    public void reduceCooldown() {
        cooldown--;
    }

    public Weapon clone() {
        return new Weapon(name, displayName, description, range, dmg, dmgType, speed, maxCooldown, spriteName, scale, hitbox, isMelee, angle);
    }

    public void resetCooldown() {
        cooldown = maxCooldown;
    }

    public boolean checkCooldown() {
        return cooldown <= 0;
    }

    public void attack(DungeonWorld world, Actor owner, double[] direction) { //double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, int lifespan, Actor me
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
