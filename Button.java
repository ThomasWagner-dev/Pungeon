import java.util.*;
import greenfoot.*;

public class Button extends Actor{
    public String name, text;
    public java.awt.Font font;
    public java.awt.Color color;

    public Button(String name, String text, java.awt.Font font, java.awt.Color color) {
        this.color = color;
        this.font = font;
        this.name = name;
        this.text = text;
        if (!text.isBlank()) {
            updateText(text);
        }
    }

    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            click_event();
        }
        if (Greenfoot.mouseDragged(this)) {
            dragBegin_event();
        }
        if (Greenfoot.mouseDragEnded(this)) {
            dragEnd_event();
        }
        if (Greenfoot.mouseMoved(this)) {
            hover_event();
        }
    }

    public void updateText(String text) {
        int[] dim = Utils.getStringDimensions(font, text);
        GreenfootImage img = new GreenfootImage(dim[0], dim[1]);
        updateText(text, img);
    }

    public void updateText(String text, GreenfootImage img) {
        this.text = text;
        Utils.drawCenteredText(img, color, font, text, img.getWidth()/2, img.getHeight()/2);
        setImage(img);
    }

    public void click_event() {}
    public void dragBegin_event() {}
    public void dragEnd_event() {}
    public void hover_event() {}
}
