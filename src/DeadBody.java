public class DeadBody extends WorldObj{

    public int lifespan = 500;

    @Override
    public void tick() {
        lifespan--;
        if (lifespan == 0) world.removeObject(this);
    }
}
