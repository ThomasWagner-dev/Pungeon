
import java.util.*;
import java.util.stream.Collectors;


public class Wpn_hitbox extends Projectile {
    List<Entity> collided = new ArrayList<>();

    /**
     * tick - do whatever the Wpn_hitbox wants to do. This method is called whenever
     * the 'tick' or 'Run' button gets pressed in the environment.
     */
    public void tick() {
        lifespan--;
        List<Entity> intersectingEntities = getTouching(Entity.class).stream().map(Entity.class::cast).collect(Collectors.toList());
        intersectingEntities.remove(this);
        intersectingEntities.remove(me);
        intersectingEntities.removeAll(collided);
        collided.addAll(intersectingEntities);
        for (Entity e : intersectingEntities) {
            System.out.println(e);
            e.collide(this);
        }
        if (lifespan <= 0)
            getWorld().removeObject(this);
    }

    public Wpn_hitbox(double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, WorldObj me) {
        super(direction, dmg, dmgType, speed, isReflective, spriteName, me);
        lifespan = 5;
        setSprite(spriteName, 1.5);
    }

    public Wpn_hitbox(double[] direction, Weapon selectedWeapon, WorldObj root) {
        super(direction, selectedWeapon, root);
        setSprite(selectedWeapon.hitbox, 1.5);
    }
}
