
/**
 * A basic Tile which can trigger intertickions.
 *
 * @author (Screxo)
 */
public class Tile extends Block {
    public void tick() {

    }

    public Block clone() {
        return topCloning(new Tile());
    }
}
