import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Counter here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Counter extends Actor
{
    public final GreenfootImage bg, prefix;
    protected String value;
    public Player p;
    
    public void act() {
        updateCounter(p.hp+"");
    }
    
    public Counter(GreenfootImage bg, GreenfootImage prefix) {
        this.bg = bg;
        this.prefix = prefix;
    }
    
    public Counter(String bg, String prefix) {
        this(new GreenfootImage(bg), new GreenfootImage(prefix));   
    }
    
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
    
    public static void load(Player p, DungeonWorld w) {
        Counter c = new Counter("counter/bg.png", "counter/hp_heart_small.png");
        c.value = p.hp+"";
        c.p = p;
        c.updateCounter();
        w.hp_counter = c;
        w.addObject(c, 40, 20);
    }
}
