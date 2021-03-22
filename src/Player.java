import java.util.*;

/**
 * The player.
 *
 * @author Commentator
 * @version 2020-12-27-22-10
 */
public class Player extends Entity implements Collider {
    protected double[] rotation;
    protected Weapon selectedWeapon;
    protected ArrayList<Weapon> inv_weapons = new ArrayList<>();
    protected Inputs inputs;
    public String skin = "entity/player/player.png";
    public String pronoun, displayname;
    public Gender gender;

    public Player(Inputs inp) {
        inputs = inp;
        inputs.p = this;
        speed = 5;
        maxhp = 20;
        hp = 20;
        dmg = 7;
        type = "physical";
        name = "player";
        rotation = new double[]{1, 0};
        //selectedWeapon = DungeonWorld.weapons.get("sword_basic");
    }

    public void tick() {
        super.tick();
        selectedWeapon.reduceCooldown();
        //check for screentransition
        if (isAtEdge()) {
            checkScreenTransition();
        }
    }

    /**
     * called if an item is collected
     * @param i the item collected
     */
    public void collect(Item i) {
        if (i.instant) {
            switch (i.changing) {
                case "hp":
                    hp = Math.min(maxhp, hp + Integer.parseInt(i.amount));
                    break;
                case "effect":
                    //TODO statuseffects: change effects
                    break;
                case "money":
                case "gold":
                    //TODO add gold/money system
                    break;
                default:
                    break;
            }
        } else {
            if (i instanceof Weapon) {
                Weapon wpn = (Weapon) i;
                inv_weapons.add(wpn);
            }
            else {
                //TODO add to inv
            }
        }
        world.removeObject(i);
    }

    /**
     * checks if one is at the edge of a screen and then waprs to the next screen
     */
    public void checkScreenTransition() {
        String edge = "";
        DungeonWorld world = (DungeonWorld) this.world;
        int mx = world.getWidth(), my = world.getHeight();
        //System.out.println("x: " + x + " y: " + y + " mx: " + mx + " my: " + my);
        if (x == 0) edge = "left";
        if (y == 0) edge = "up";
        if (x == mx - 1) edge = "right";
        if (y == my - 1) edge = "down";
        world.mg.transition(edge);
        switch (edge) {
            case "up":
                setLocation(x, my-(int)(DungeonWorld.pixelSize*1.5));
                break;
            case "down":
                setLocation(x, (int)(DungeonWorld.pixelSize*1.5));
                break;
            case "right":
                setLocation((int)(DungeonWorld.pixelSize*1.5), y);
                break;
            case "left":
                setLocation(mx - (int)(DungeonWorld.pixelSize*1.5), y);
        }
    }

    /**
     * attacks. see attack(world, owner, rotation) in weapon for more information
     */
    public void attack() {
        selectedWeapon.attack((DungeonWorld) world, this, rotation);
    }

    /**
     * selects the new weapon w
     */
    public void selectWeapon(Weapon w) {
        selectedWeapon = w; //set weapon as new selected weapon.
        AdvancedImage img = DungeonWorld.scaleImage(Utils.loadImageFromAssets(skin), 1), wImg = w.img;
        //wImg.rotate(-45);
        img.drawImage(w.img.rotate(45), 0, 15);
        //wImg.rotate(45);
        setImage(img);
    }

    protected double[][] getMovement() {
        double[][] movement = inputs.getMovement();
        if (movement[0][0] != 0 || movement[0][1] != 0)
            rotation = movement[0];
        return movement;
    }

    /**
     * called when the player dies. shows death screen and reloads latest save
     */
    public void die() {
        DungeonWorld origin = (DungeonWorld) world;
        origin.menuscrn.showGameover(this);
        origin.removeObjectsExclusive(Counter.class);
        FileWork.loadPlayer(origin.selectedSave, origin, this);
    }

    public void keyPressed(char key) {
        if (key == inputs.keybinds.attack)
            attack();
        else if (Character.toLowerCase(key) == inputs.keybinds.w_cycle_f)
            selectWeapon(inv_weapons.get((inv_weapons.indexOf(selectedWeapon) + 1) % inv_weapons.size()));
        else if (Character.toLowerCase(key) == inputs.keybinds.w_cycle_b) {
            int ind = inv_weapons.indexOf(selectedWeapon) - 1;
            if (ind < 0) ind += inv_weapons.size();
            selectWeapon(inv_weapons.get(ind));
        }
        else if (key == inputs.keybinds.pause)
            ((DungeonWorld) world).menuscrn.showEscape();
        else if (key == inputs.keybinds.map) {
            DungeonWorld w = (DungeonWorld) world;
            Menuscreen.showMap(w, inputs, w);
        }
    }
}
