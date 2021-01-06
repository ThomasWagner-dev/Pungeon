import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The healthbar for the Player.
 */
public class Counter extends Actor
{
    public final GreenfootImage bg, prefix;
    protected String value;
    public Player p;

    /**
     * Updates the healthbar of the player every tick.
     */
    public void act() {
        updateCounter(p.hp+"");
    }

    /**
     * Basic constructor for the healtbar using 2 Greenfootimages.
     *
     * @param bg TODO
     * @param prefix TODO
     */
    public Counter(GreenfootImage bg, GreenfootImage prefix) {
        this.bg = bg;
        this.prefix = prefix;
    }

    /**
     * Basic constructor for the healtbar using the name of 2 Greenfootimages.
     *
     * @param bg TODO
     * @param prefix TODO
     */
    public Counter(String bg, String prefix) {
        this(new GreenfootImage(bg), new GreenfootImage(prefix));   
    }

    /**
     * Updates the Healthbar adding or substracting the given value.
     *
     * @param value The value to change the Healthbar by.
     */
    public void updateCounter(String value) {
        int xs = prefix.getWidth()+20, ys = prefix.getHeight()+10;
        //System.out.println("xs: "+ xs + " ys: " + ys);
        GreenfootImage bg = new GreenfootImage(this.bg);
        GreenfootImage text = new GreenfootImage(value, 22, Color.BLACK, new Color(0,0,0,0));
        xs += text.getWidth();
        ys = text.getHeight()+10 > ys? text.getHeight()+10 : ys;
        
        bg.scale(xs, ys);
        bg.drawImage(prefix, 7, 10);
        bg.drawImage(text, prefix.getWidth()+10, 05);
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
        c.value = p.hp+"";
        c.p = p;
        c.updateCounter();
        w.hp_counter = c;
        w.addObject(c, 40, 20);
    }
}
