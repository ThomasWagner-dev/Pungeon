/**
 * The super class for all Blocks.
 */
public abstract class Block extends WorldObj implements Cloneable {
    /**
     * A public version of the protected clone() method of Object. Allows blocks to be cloned, making loading easier.
     */
    public abstract Block clone();

    protected Block topCloning(Block b) {
        b.setImage(img);
        return b;
    }

    public void tick() {}
}
