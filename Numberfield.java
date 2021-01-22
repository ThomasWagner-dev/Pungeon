import greenfoot.*;
import java.util.*;

public class Numberfield extends Button {


    public int value = 0, changing, min, max;


    public Numberfield(String name, int value, java.awt.Font font, java.awt.Color color) {
        super(name, value+"", font, color);
        this.value = value;
    }

    public Numberfield(String name, int value, int changing, int min, int max, java.awt.Font font, java.awt.Color color ) {
        super(name, value+"", font, color);
        this.value = value;
        this.changing = changing;
        this.min = min;
        this.max = max;
    }


    public void click_event() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        int changing = Greenfoot.isKeyDown("shift")? this.changing/2:this.changing;
        int v = 0;
        switch (mouse.getButton()) {
            case 1:
                v = value + changing;
                break;
            case 3:
                v = value - changing;
                break;
        }
        if (v < min) {
            v = max;
        }
        if (v > max) {
            v = min;
        }
        updateValue(v);
        on_click();
    }

    public void updateValue(int value) {
        this.value = value;
        updateText(value+"");
    }

    public void on_click() {

    }

}
