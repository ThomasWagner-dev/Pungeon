public class Chest extends Enemy implements Collider {



    public Chest(String name) {
        super(name);
        type = "physical";
    }

    public void tick() {}

    public void attack() {}

    public Enemy clone() {
        return topClone(new Chest(name), this);
    }

    @Override
    protected double[][] getMovement() {
        return new double[][] {{0,0},{0}};
    }
}
