import java.util.*;
import java.util.stream.Collectors;

/**
 * Projectiles. Flying objects which move through space and deal damage.
 *
 * @author Commentator
 */
public class Projectile extends Entity {
    public double[] direction;
    public final boolean isReflective;
    //protected final int dmg;
    //protected final String dmgType;
    protected int lifespan;
    protected WorldObj me;
    public String dmgType;
    // public Projectile(int[] direction, int dmg, String dmgType) {
    // new Projectile(direction, dmg, dmgType, false);
    // }

    /**
     * moves and then checks for all collisions. {@link Entity#collide(Projectile)} in Entity for further detail
     */
    public void tick() {
        super.tick();
        lifespan--;
        if (lifespan == 0) {
            world.removeObject(this);
            return;
        }
        List<Wall> intersectingWalls = getTouching(Wall.class).stream().map(Wall.class::cast).collect(Collectors.toList());
        List<Entity> intersectingEntities = getTouching(Entity.class).stream().map(Entity.class::cast).collect(Collectors.toList());
        intersectingEntities.remove(this);
        intersectingEntities.remove(me);
        //System.out.println(intersectingEntities);
        //getNeighbours(1, true, null)
        boolean collided = false;
        for (Wall w : intersectingWalls) {
            if (!collided) {
                w.collide(this);
                collided = true;
            }
        }

        for (Entity e : intersectingEntities) {
            if (!collided) {
                e.collide(this);
                collided = true;
            }
        }
    }

    public Projectile(double[] direction, Weapon selectedWeapon, WorldObj root) {
        this.direction = direction;
        dmg = selectedWeapon.dmg;
        dmgType = selectedWeapon.dmgType;
        speed = selectedWeapon.speed;
        isReflective = false;
        setSprite(selectedWeapon.hitbox);
        lifespan = selectedWeapon.range == 0 ? 10 : (int) ((selectedWeapon.range * DungeonWorld.pixelSize) / speed) + 1;
        this.me = root;
        setRotation(DungeonWorld.getRotationAngle(direction));
    }

    public Projectile(double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, int lifespan, WorldObj me) {
        //setRotation((int) Math.acos((direction[0])/(Math.sqrt(direction[0]*direction[0]+direction[1]*direction[1])))*90+90);
        //System.out.println(getRotation());
        this.direction = direction;
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
        this.lifespan = lifespan;
        this.me = me;
        this.speed = speed;
        setRotation(DungeonWorld.getRotationAngle(direction));

        setSprite(spriteName);
    }

    public Projectile(double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, WorldObj me) {
        this.direction = direction;
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
        this.lifespan = -1;
        this.me = me;
        this.speed = speed;
        setRotation(DungeonWorld.getRotationAngle(direction));

        setSprite(spriteName);
    }


    protected double[][] getMovement() {
        return new double[][]{direction, {1}};
    }
}
