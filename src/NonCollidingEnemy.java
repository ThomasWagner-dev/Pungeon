
/**
 * Write a description of class NonCollidingEnemy here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class NonCollidingEnemy extends Enemy {
    public NonCollidingEnemy(String name) {
        super(name);
    }

    /**
     * defines standart movement for all melee enemies: Walk directly towards the player and ignore walls or others.
     */
    public double[][] getMovement() {
        DungeonWorld world = (DungeonWorld) this.world;
        Player player = (Player) world.getClosestObject(Player.class, this);
        if (inRange(player)) return new double[][]{{0, 0}, {1}};
        return new double[][]{{player.x - x, player.y - y}, {1}};
    }

    /**
     * returns a clone of this enemy
     */
    public Enemy clone() {
        return super.topClone(new NonCollidingEnemy(name), this);
    }
}
