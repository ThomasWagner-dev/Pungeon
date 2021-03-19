
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Commentator
 * @version 2020-11-24-1207
 */
public class Trap extends Block {
    public final int dmg, cooldown, range;
    public final String dmgType, trigger;
    private int currentCooldown;
    public final String intickiveImg, tickiveImg;

    public Trap(int range, String intickiveImg, String tickiveImg, int dmg, String dmgType, String trigger, int cooldown) {
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.cooldown = cooldown;
        this.range = range;
        currentCooldown = 0;
        this.trigger = trigger;
        this.intickiveImg = intickiveImg;
        this.tickiveImg = tickiveImg;
    }


    public void tick() {
        currentCooldown--;
        if (currentCooldown > 0) return;

        List<Entity> attackingObjects = objectsInRange(range, Entity.class).stream().map(Entity.class::cast).collect(Collectors.toList());
        if (attackingObjects.size() > 0) {
            attackingObjects.forEach(e -> e.takeDamage(this));
            //TODO: play animation
            currentCooldown = cooldown;
        }
    }

    public Block clone() {
        return topCloning(new Trap(range, intickiveImg, tickiveImg, dmg, dmgType, trigger, cooldown));
    }
}
