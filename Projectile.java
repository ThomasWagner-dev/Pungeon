import java.util.*;

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    protected Actor me;
    public String dmgType;
    // public Projectile(int[] direction, int dmg, String dmgType) {
    // new Projectile(direction, dmg, dmgType, false);
    // }

    /**
     * moves and then checks for all collisions. See collide(Projectile p) in Entity for further detail
     */
    public void act() {
        super.act();
        lifespan--;
        if (lifespan == 0) {
            getWorld().removeObject(this);
            return;
        }
        List<Wall> intersectingWalls = getIntersectingObjects(Wall.class);
        List<Entity> intersectingEntities = getIntersectingObjects(Entity.class);
        intersectingWalls.remove(this);
        intersectingEntities.remove(this);
        intersectingEntities.remove(me);
        //System.out.println(intersectingEntities);
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

    public Projectile(double[] direction, Weapon selectedWeapon, Actor root) {
        this.direction = direction;
        dmg = selectedWeapon.dmg;
        dmgType = selectedWeapon.dmgType;
        speed = selectedWeapon.speed;
        isReflective = false;
        setSprite(selectedWeapon.hitbox);
        lifespan = selectedWeapon.range == 0 ? 10 : (int) ((selectedWeapon.range * DungeonWorld.pixelSize) / speed) + 1;
        this.me = root;
    }

    public Projectile(double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, int lifespan, Actor me) {
        //setRotation((int) Math.acos((direction[0])/(Math.sqrt(direction[0]*direction[0]+direction[1]*direction[1])))*90+90);
        //System.out.println(getRotation());
        this.direction = direction;
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
        this.lifespan = lifespan;
        this.me = me;
        this.speed = speed;

        setSprite(spriteName);
    }

    public Projectile(double[] direction, int dmg, String dmgType, int speed, boolean isReflective, String spriteName, Actor me) {
        this.direction = direction;
        this.dmg = dmg;
        this.dmgType = dmgType;
        this.isReflective = isReflective;
        this.lifespan = -1;
        this.me = me;
        this.speed = speed;

        setSprite(spriteName);
    }


    protected double[][] getMovement() {
        return new double[][]{direction, {1}};
    }
}
