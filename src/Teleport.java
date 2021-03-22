import java.util.ArrayList;

public class Teleport extends Tile {

    public Teleport() {
        setImage(Utils.loadImageFromAssets("map/tile/teleport.png").scale(64,64));
    }

    public void tick() {
        if (isTouching(Player.class)) {
            DungeonWorld w = (DungeonWorld) world;
            w.menuscrn.showNextLevel();
        }
    }

}
