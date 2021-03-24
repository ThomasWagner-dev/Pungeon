import java.awt.*;
import java.awt.image.RescaleOp;

public class Indicator extends WorldObj {

    public AdvancedImage orig;
    public int lifetime;
    public int t;
    public Color c;
    public AttackIndication caller;

    public Indicator(AdvancedImage orig, Color c, int lifetime, AttackIndication caller) {
        this.orig = orig;
        this.lifetime = lifetime;
        this.c = c;
        this.caller = caller;
    }

    @Override
    public void tick() {
        t++;
        if (t > lifetime) {
            world.removeObject(this);
            caller.action();
            return;
        }
        Color nc = t > lifetime/2? c.brighter():c.darker();

        setImage(orig.replaceColor(c, nc));
        c = nc;
    }
}
