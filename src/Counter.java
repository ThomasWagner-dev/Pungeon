import java.awt.*;

/**
 * The healthbar for the Player.
 */
public class Counter extends WorldObj {
    public final AdvancedImage bg, prefix;
    protected String value;
    public Player p;

    /**
     * Updates the healthbar of the player every tick.
     */
    public void tick() {
        updateCounter(p.hp + "");
    }

    /**
     * Basic constructor for the healtbar using 2 AdvancedImages.
     *
     * @param bg     background image
     * @param prefix image which always gets put in front of the value
     */
    public Counter(AdvancedImage bg, AdvancedImage prefix) {
        this.bg = bg;
        this.prefix = prefix;
    }

    /**
     * Basic constructor for the healtbar using the name of 2 AdvancedImages.
     *
     * @param bg     background image
     * @param prefix text which always gets put in front of the value
     */
    public Counter(String bg, String prefix) {
        this(Utils.loadImageFromAssets(bg), Utils.loadImageFromAssets(prefix));
    }

    /**
     * Updates the Healthbar adding or substrticking the given value.
     *
     * @param value The value to change the Healthbar by.
     */
    public void updateCounter(String value) {
        int xs = prefix.getWidth() + 20, ys = prefix.getHeight() + 10;
        //System.out.println("xs: "+ xs + " ys: " + ys);
        AdvancedImage bg = new AdvancedImage(this.bg);
        AdvancedImage text = new AdvancedImage(value, world==null?new Font(Font.SERIF, Font.PLAIN, 22):((DungeonWorld)world).awtfonts.get("Welbut").deriveFont(22F), Color.BLACK, new Color(0, 0, 0, 0));
        xs += text.getWidth();
        ys = Math.max(text.getHeight() + 10, ys);

        bg = bg.scale(xs, ys);
        bg.drawImage(prefix, 7, 10);
        bg.drawImage(text, prefix.getWidth() + 10, 05);
        setImage(bg);
    }

    public void updateCounter() {
        updateCounter(value);
    }

    /**
     * Load the healthbar for a given player and dungeonworld.
     *
     * @param p The player.
     * @param w The dungeonworld.
     */
    public static void load(Player p, DungeonWorld w) {
        Counter c = new Counter("counter/bg.png", "counter/hp_heart_small.png");
        c.value = p.hp + "";
        c.p = p;
        c.updateCounter();
        w.hp_counter = c;
        w.addObject(c, 40, 20);
    }
}
