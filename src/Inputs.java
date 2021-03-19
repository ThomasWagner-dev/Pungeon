
import java.util.*;

/**
 * Class to fetch inputs.
 * Required to make controller accessebility easier to add afterwards.
 *
 * @author Commentator
 */
public class Inputs {
    public KeyLayout keybinds;
    public Player p;
    public ArrayList<String> pressed_keys = new ArrayList<>();

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
        if (Utils.isKeyDown('w')) {
            movement[1] -= 1;
        }
        if (Utils.isKeyDown('s')) {
            movement[1] += 1;
        }
        if (Utils.isKeyDown('a')) {
            movement[0] -= 1;
        }
        if (Utils.isKeyDown('d')) {
            movement[0] += 1;
        }

        if (Utils.isShiftDown()) {
            speedmultiplier = 2;
        }
        if (Utils.isControlDown()) {
            speedmultiplier = 0.5;
        }

        return new double[][]{movement, new double[]{speedmultiplier}};
    }


//    public void checkKeys() {
//        if (checkKey(keybinds.attack)) {
//            p.attack();
//        }
//        if (checkKey(keybinds.w_cycle_f)) {
//            p.selectWeapon(p.inv_weapons.get((p.inv_weapons.indexOf(p.selectedWeapon) + 1) % p.inv_weapons.size()));
//        }
//        if (checkKey(keybinds.w_cycle_b) ) {
//            int ind = p.inv_weapons.indexOf(p.selectedWeapon) - 1;
//            if (ind < 0) ind += p.inv_weapons.size();
//            p.selectWeapon(p.inv_weapons.get(ind));
//        }
//        if (checkKey(keybinds.pause)) {
//            ((DungeonWorld) p.getWorld()).menuscrn.showEscape();
//        }
//        if (checkKey(keybinds.map)) {
//            DungeonWorld w = (DungeonWorld) p.getWorld();
//            w.menuscrn.showMap(w, this, w);
//        }
//    }
//
//    public boolean checkKey(String key) {
//        if (key == null) return Greenfoot.getKey() != null;
//        if (Utils.isKeyDown(key)) {
//            if (! pressed_keys.contains(key)) {
//                pressed_keys.add(key);
//                return true;
//            }
//        }
//        else {
//            pressed_keys.remove(key);
//        }
//        return false;
//    }
//
//    public String getCurrentKey() {
//        String key = Greenfoot.getKey();
//        //if (!checkKey(key)) return "";
//        if (key == null) return "";
//        if (key.equals("shift") ||
//                key.equals("control") ||
//                key.equals("enter") ||
//                key.equals("tab") ||
//                key.equals("backspace") ||
//                key.equals("up") ||
//                key.equals("down") ||
//                key.equals("left") ||
//                key.equals("right") ||
//                key.equals("escape")
//            ) return key;
//        for (int i = 0; i < 12; i ++) {
//            if (key.equals("F"+i)) return "";
//        }
//        if (key.equals("space")) {
//            return " ";
//        }
//        if (Greenfoot.isKeyDown("shift")) {
//            return key.toUpperCase();
//        }
//        else return key;
//    }
}
