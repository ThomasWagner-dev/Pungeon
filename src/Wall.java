
/**
 * Walls. What do you Expect?
 *
 * @author Commentator
 */
public class Wall extends Block implements Collider {
    public final boolean breakable;

    public Wall() {
        breakable = false;
    }

    public Wall(boolean breakable) {
        this.breakable = breakable;
    }

    /**
     * Collision handling for walls. Gets called by the projectile.
     */
    public void collide(Projectile p) {
        //parsed world the wall is in
        DungeonWorld world = (DungeonWorld) getWorld();

        //check if the Projectile is reflected by walls
        if (p.isReflective) {
            //Big TODO: get collision face and invert accordingly!
            p.direction = new double[]{p.direction[0] * -1, p.direction[1] * -1};
        } else {
            world.removeObject(p);
        }
        if (breakable) {
            world.removeObject(this);
        }
    }

    public Block clone() {
        return topCloning(new Wall(breakable));
    }
}
