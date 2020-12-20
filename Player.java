 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * The player.
 * 
 * @author Commentator
 */
public class Player extends Entity implements Collider{
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
        rotation = new double[] {0,0};
        //selectedWeapon = DungeonWorld.weapons.get("sword_basic");
    }
    
    public void act() {
        super.act();
        selectedWeapon.cooldown--;
        if (selectedWeapon.cooldown <= 0 && inputs.attacks()) {
            attack();
        }
    }
    
    public void attack() {
        selectedWeapon.cooldown = selectedWeapon.maxCooldown;
        selectedWeapon.attack(getWorld(), this, rotation);
        //getWorld().addObject(p, getX(), getY());
    }
    
    public void selectWeapon(Weapon w) {
        selectedWeapon = w;
        GreenfootImage img = DungeonWorld.scaleImage(new GreenfootImage(skin),1);
        img.drawImage(w.img,0,0);
        setImage(img);
    }
    
    protected double[][] getMovement() {
        double[][] movement = inputs.getMovement();
        if (movement[0][0] != 0 || movement[0][1] != 0)
            rotation = movement[0];
        return movement;
    }
}
