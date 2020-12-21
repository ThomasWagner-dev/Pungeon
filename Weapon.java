import greenfoot.*;
/**
 * Weapons. They attack. 
 * 
 * @author Commentator
 */
public class Weapon  
{
    public final int range, dmg, maxCooldown, speed;
    public final double scale;
    public final String name, description, dmgType, displayName;
    public final String spriteName, hitbox;
    public final GreenfootImage img;
    public int cooldown;
    
    public Weapon(String name, String displayname, String description, int range, int dmg, String dmgType, int speed, int cooldown, String spriteName, String hitbox) {
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
        this.scale = 1;
        this.displayName = displayname;
        img = DungeonWorld.scaleImage(new GreenfootImage(spriteName),scale);
    }
    
    public Weapon(String name, String displayname, String description, int range, int dmg, String dmgType, int speed, int cooldown, String spriteName, double scale, String hitbox) {
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
        img = DungeonWorld.scaleImage(new GreenfootImage(spriteName),scale);
    }
    
    public void reduceCooldown() {
        cooldown = cooldown == 0? 0 : cooldown-1;
    }
    
    public void resetCooldown() {
        cooldown = maxCooldown;
    }
    
    public boolean checkCooldown() {
        return cooldown <= 0;
    }
    
    public void attack(World world, Actor owner, double[] direction) { //double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, int lifespan, Actor me
        Projectile p = new Projectile(
            direction,
            this,
            owner
        );
        p.setRotation(DungeonWorld.getRotationAngle(direction));
        world.addObject(p, owner.getX(), owner.getY());
        p.move(DungeonWorld.pixelSize/2);
    }
}
