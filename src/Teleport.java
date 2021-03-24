import java.util.ArrayList;

public class Teleport extends Tile {

    public boolean enabled = false;

    public Teleport() {
        setImage(Utils.loadImageFromAssets("map/tile/teleport_sealed.png").scale(64,64));
    }

    public void tick() {
        if (enabled && isTouching(Player.class)) {
            DungeonWorld w = (DungeonWorld) world;
            w.menuscrn.showNextLevel();
        }
    }

    public void unseal() {
        enabled = true;
        setImage(Utils.loadImageFromAssets("map/tile/teleport.png").scale(64,64));
    }

}
