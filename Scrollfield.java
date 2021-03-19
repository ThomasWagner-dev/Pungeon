import java.util.*;
import greenfoot.*;

public class Scrollfield extends Button {
    public List<String> options;
    public String selected;

    public Scrollfield(String name, List<String> options, java.awt.Font font, java.awt.Color color) {
        super(name, options.size() == 0? "" : options.get(0), font, color);
        this.options = options;
        selected = options.size()==0?null: options.get(0);

    }

    public void click_event() {
        int s = options.size();
        if (s <= 1) return;
        switch(Greenfoot.getMouseInfo().getButton()) {
            case 1:
                selected = options.get((options.indexOf(selected)+1)%s);
                break;
            case 3:
                selected = options.get((options.indexOf(selected)+(s-1))%s);
                break;
        }
        updateText(selected);
    }
}
