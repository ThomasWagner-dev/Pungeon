

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player.
 *
 * @author Commentator
 * @version 2020-12-27-22-10
 */
public class Player extends Entity implements Collider {
    protected double[] rotation;
    protected Weapon selectedWeapon;
    protected Inputs inputs = new Inputs(new KeyLayout());
    public String skin = "entity/player/player.png";

    public Player() {
        speed = 5;
        maxhp = 20;
        hp = 20;
        dmg = 7;
        type = "physical";
        rotation = new double[]{0, 0};
        //selectedWeapon = DungeonWorld.weapons.get("sword_basic");
    }

    public void act() {
        super.act();
        selectedWeapon.reduceCooldown();
        //check for screentransition
        if (isAtEdge()) {
            checkScreenTransition();
        }
        if (inputs.attacks()) {
            attack();
        }
    }

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
            //TODO add to inv
        }
        getWorld().removeObject(i);
    }

    public void checkScreenTransition() {
        String edge = "";
        DungeonWorld world = (DungeonWorld) getWorld();
        int x = getX(), y = getY(), mx = world.getWidth(), my = world.getHeight();
        //System.out.println("x: " + x + " y: " + y + " mx: " + mx + " my: " + my);
        if (x == 0) edge = "left";
        if (y == 0) edge = "up";
        if (x == mx - 1) edge = "right";
        if (y == my - 1) edge = "down";
        String nextScreenName = world.activeScreen.adjacentScreens.get(edge);
        if (nextScreenName != null) {
            Screen nextScreen = world.screens.get(nextScreenName);
            nextScreen.load(world);
            if (edge.equals("up") || edge.equals("down")) {
                setLocation(x, my - (y - world.pixelSize / 2));
            } else {
                setLocation(mx - (x - world.pixelSize / 2), y);
            }
        }
    }

    /**
     * attacks. see attack(world, owner, rotation) in weapon for more information
     */
    public void attack() {
        selectedWeapon.attack((DungeonWorld) getWorld(), this, rotation);
    }

    /**
     * selects the new weapon w
     */
    public void selectWeapon(Weapon w) {
        selectedWeapon = w; //set weapon as new selected weapon.
        GreenfootImage img = DungeonWorld.scaleImage(new GreenfootImage(skin), 1), wImg = w.img;
        wImg.rotate(-45);
        img.drawImage(w.img, 0, 15);
        wImg.rotate(45);
        setImage(img);
    }

    protected double[][] getMovement() {
        double[][] movement = inputs.getMovement();
        if (movement[0][0] != 0 || movement[0][1] != 0)
            rotation = movement[0];
        return movement;
    }

    public void die() {
        ((DungeonWorld) getWorld()).menuscrn.showGameover(this);
    }
}
