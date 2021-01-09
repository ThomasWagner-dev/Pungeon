
import java.util.*;
import greenfoot.*;

/**
 * Class to fetch inputs.
 * Required to make controlleraccessebility easier to add afterwards.
 *
 * @author Commentator
 */
public class Inputs {
    KeyLayout keybinds;
    Player p;
    ArrayList<String> pressed_keys = new ArrayList<>();

    public Inputs(KeyLayout keybinds, Player p) {
        this.keybinds = keybinds;
        this.p = p;
    }


    /**
     * fetches momement multiplier.
     *
     * @return movement multiplier as [[x,y],[speedmultiplier]]
     */
    public double[][] getMovement() {
        double[] movement = new double[2];
        double speedmultiplier = 1;
        if (Greenfoot.isKeyDown("w")) {
            movement[1] -= 1;
        }
        if (Greenfoot.isKeyDown("s")) {
            movement[1] += 1;
        }
        if (Greenfoot.isKeyDown("a")) {
            movement[0] -= 1;
        }
        if (Greenfoot.isKeyDown("d")) {
            movement[0] += 1;
        }

        if (Greenfoot.isKeyDown("shift")) {
            speedmultiplier = 2;
        }
        if (Greenfoot.isKeyDown("ctrl")) {
            speedmultiplier = 0.5;
        }
        //System.out.println(movement[0] + "," + movement[1]);
        return new double[][]{movement, new double[]{speedmultiplier}};
    }


    public void checkKeys() {
        if (Greenfoot.isKeyDown(keybinds.attack)) {
            if (! pressed_keys.contains(keybinds.attack)) {
                pressed_keys.add(keybinds.attack);
                p.attack();
            }
        }
        else {
            pressed_keys.remove(keybinds.attack);
        }
        if (Greenfoot.isKeyDown(keybinds.w_cycle_f) ) {
            if (! pressed_keys.contains(keybinds.w_cycle_f)) {
                p.selectWeapon(p.inv_weapons.get((p.inv_weapons.indexOf(p.selectedWeapon) + 1) % p.inv_weapons.size()));
                pressed_keys.add(keybinds.w_cycle_f);
            }
        }
        else {
            pressed_keys.remove(keybinds.w_cycle_f);
        }
        if (Greenfoot.isKeyDown(keybinds.w_cycle_b) ) {
            if (! pressed_keys.contains(keybinds.w_cycle_b)) {
                int ind = p.inv_weapons.indexOf(p.selectedWeapon) - 1;
                if (ind < 0) ind += p.inv_weapons.size();
                p.selectWeapon(p.inv_weapons.get(ind));
                pressed_keys.add(keybinds.w_cycle_b);
            }
        }
        else {
            pressed_keys.remove(keybinds.w_cycle_b);
        }
    }
}
