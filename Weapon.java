/**
 * Weapons. They attack. 
 * 
 * @author Commentator
 */
public class Weapon  
{
    public final int range, dmg, maxCooldown, speed;
    public final String name, description, dmgType;
    public final String spriteName;
    public int cooldown;
    
    public Weapon(String name, String description, int range, int dmg, String dmgType, int speed, String spriteName, int cooldown) {
        this.range = range;
        this.name = name;
        this.description = description;
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.spriteName = spriteName;
        this.cooldown = 0;
        this.maxCooldown = cooldown;
        this.speed = speed;
    }
}
