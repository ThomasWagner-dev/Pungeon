import greenfoot.*;

/**
 * An actor for text input in Greenfoot.
 * This code was copied from https://www.greenfoot.org/topics/2424 fixed and updated.
 * @author Sven van Nigtevecht/Sl3nDeRm4n (Updated to Greenfoot 3.6 by Commentator)
 * @version 1.1
 */
public class Textfield extends Button {
    private GreenfootImage beginImage;
    public String name;
    public boolean isSelected = false;
    public boolean numeric = false;
    public Inputs inp;

    public Textfield(String name, String text, java.awt.Font font, java.awt.Color color, Inputs inp) {
        this(name, text, font, color, false, inp);
    }

    /**
     * Create a new text field with a width of 400.
     */
    public Textfield(String name, String text, java.awt.Font font, java.awt.Color color, boolean numeric, Inputs inp) {
        this(400, name, text, font, color, numeric, inp);
    }

    /**
     * Create a new text field with a given width.
     */
    public Textfield(int width, String name, String text, java.awt.Font font, java.awt.Color color, boolean numeric, Inputs inp) {
        super(name, text, font, color);
        this.numeric = numeric;
        this.inp = inp;
        GreenfootImage img = new GreenfootImage(width, 50);
        img.setColor(Color.WHITE);
        img.fill();
        img.setColor(Color.BLACK);
        img.fillRect(3, 3, img.getWidth() - 6, img.getHeight() - 6);
        setImage(img);
        this.beginImage = img;
        this.name = name;
        this.font = font.deriveFont(40.0F);
        updateText(text);
    }

    /**
     * Returns the text of the text field.
     */
    public String getText() {
        if (this.text != null) return this.text;
        return "";
    }

    /**
     * Returns the image of the text field, without the text.
     */
    public GreenfootImage getBeginImage() {
        return this.beginImage;
    }

    /**
     * Add text onto the text field if needed.
     */
    public void act() {
        super.act();
        if (isSelected)
            addInput();

    }

    private void addInput() {

        String key = inp.getCurrentKey();
        if (key == null) return;
        if (key.equals("enter") || key.equals("escape")) {
            deselect();
            return;
        }
        if (key.equals("backspace")) {
            if (text.length() > 0)
                text = text.substring(0, text.length() - 1);
        } else {
            if (key.equals("shift") ||
                    key.equals("control") ||
                    key.equals("tab")) {
                key = "";
            }
            if (numeric && !"0123456789".contains(key)) key = "";
            text = text + key;
        }
        refresh();
    }

    /**
     * Redraw the image of the text field.
     */
    public void refresh() {
        GreenfootImage img = new GreenfootImage(beginImage);
        updateText(text, img);
    }

    public void click_event() {
        getWorld().getObjects(Textfield.class).forEach(tf -> tf.deselect());
        select();
    }

    public void select() {
        GreenfootImage img = new GreenfootImage(beginImage);
        img.setColor(Color.GREEN);
        img.fill();
        img.setColor(Color.BLACK);
        img.fillRect(3, 3, img.getWidth() - 6, img.getHeight() - 6);
        setImage(img);
        this.beginImage = img;
        refresh();
        isSelected = true;
    }

    public void deselect() {
        GreenfootImage img = new GreenfootImage(beginImage);
        img.setColor(Color.WHITE);
        img.fill();
        img.setColor(Color.BLACK);
        img.fillRect(3, 3, img.getWidth() - 6, img.getHeight() - 6);
        setImage(img);
        this.beginImage = img;
        refresh();
        isSelected = false;
    }
}