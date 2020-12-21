 

import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
/**
 * Superclass for all moving Objects.
 */ 
public abstract class Entity extends Actor
{
    protected int hp, maxhp, dmg, attackcooldown, currentcooldown;
    protected double speed;
    protected String  type;
    protected ArrayList<String> activeEffects;
    
    /**
     * creates objects for everything which is the same everywhere
     */
    public Entity() {
        GreenfootImage tmp = getImage();
        tmp.scale(DungeonWorld.pixelSize,DungeonWorld.pixelSize);
        setImage(tmp);
        currentcooldown = 0;
        activeEffects = new ArrayList<>();
    }
    
    /**
     * Yes
     */
    public void act() {
        move();
        currentcooldown = currentcooldown == 0? 0:currentcooldown-1;
    }
    
    /**
     * moves the entity. If it is a Collider, it steps back elsewise colliding with an Object
     */
    protected void move() {
        double[][] movementValues = getMovement(); //fetch movementvalues as [[x,y],[speedmultiplier]]
        double speedmultiplier = movementValues[1][0]; //saves speedmultiplier
        double[] movementOriginal = getLimitedMovement(movementValues[0]); //limits movement to a distance of 1
        double[] movement = new double[] {movementOriginal[0]*speed*speedmultiplier, movementOriginal[1]*speed*speedmultiplier}; //calculates movementvector using the limited vector and speed
        //System.out.println(Arrays.toString(movementOriginal));
        double oldX = getX(),
            oldY = getY(),
            newX = oldX + movement[0],
            newY = oldY + movement[1];
        
        double workX = newX,
            workY = newY;
        //moves Entity to final location
        setLocation((int) newX, (int)newY);
        
        //skips back movement if the entity doesn't have collision
        if (!(this instanceof Collider)) return;
        
        //checks goes back, until Entity ain't colliding anymore
        while (isTouching(Collider.class)) {
            if (workX == oldX && workY == oldY) 
                break;
            
            //workX -= movementOriginal[0]; //Original movement to keep the ratio between x and y
            //workY -= movementOriginal[1];
            setLocation((int) oldX, (int) oldY);
            return;
        }
        setLocation((int) workX, (int) workY);
    }
    
    /**
     * gets movement as a 2d vector, dependent on either player input or AI
     */
    protected abstract double[][] getMovement();
    
    /**
     * Basic collision physics. check act() in Projectile for further information
     */
    protected void collide(Projectile p) {
        DungeonWorld world = (DungeonWorld) getWorld();
        world.removeObject(p);
        System.out.println(world.dmgMultiplier.keySet()+ " me: "+ type + " e:" +p.dmgType);
        //System.out.println("e.dmgType: "+ e.dmgType + "; type: "+type + "; dmg: ");//+(world.dmgMultiplier.get(e.dmgType).keySet()));
        double dmgMultiplier = world.dmgMultiplier.get(p.dmgType).get(type);
        //reduces entity hp by the amount of damage, dependent on the damage multipliers, fetched from the dmgMultipliers.stats file.
        System.out.println("dmg: "+p.dmg + " mulitplier: "+dmgMultiplier);
        System.out.println("hp before: "+hp);
        hp -= p.dmg * dmgMultiplier;
        System.out.println("hp after: "+hp);        
        // kills the entity, if hp is less or equal to 0
        if (hp <= 0) {
            die();
        }
    }
    
    /**
     * makes the Entity take damage from the @param[attacking entity].
     * If hp is 0 or less afterwards, the dies and gets removed.
     */
    protected void takeDamage(Weapon w) {
        DungeonWorld world = (DungeonWorld) getWorld();
        System.out.println(world.dmgMultiplier.keySet()+ " me: "+ type + " e:" +w.dmgType);
        //System.out.println("e.dmgType: "+ e.dmgType + "; type: "+type + "; dmg: ");//+(world.dmgMultiplier.get(e.dmgType).keySet()));
        double dmgMultiplier = world.dmgMultiplier.get(w.dmgType).get(type);
        //reduces entity hp by the amount of damage, dependent on the damage multipliers, fetched from the dmgMultipliers.stats file.
        System.out.println("dmg: "+w.dmg + " mulitplier: "+dmgMultiplier);
        System.out.println("hp before: "+hp);
        hp -= w.dmg * dmgMultiplier;
        System.out.println("hp after: "+hp);        
        // kills the entity, if hp is less or equal to 0
        if (hp <= 0) {
            die();
        }
    }
    
    protected void takeDamage(Trap t) {
        DungeonWorld world = (DungeonWorld) getWorld();
        System.out.println(world.dmgMultiplier.keySet());
        //System.out.println("t.dmgType: "+ t.dmgType + "; type: "+type + "; dmg: ");//+(world.dmgMultiplier.get(e.dmgType).keySet()));
        //reduces entity hp by the amount of damage, dependent on the damage multipliers, fetched from the dmgMultipliers.stats file.
        hp -= t.dmg * world.dmgMultiplier.get(t.dmgType).get(type);
        
        // kills the entity, if hp is less or equal to 0
        if (hp <= 0) {
            die();
        }
    }
    
    /**
     * executed if the entity dies. Removes object from world (and plays animation, if redifined in subclasses)
     */
    protected void die() {
        getWorld().removeObject(this);
    }
    
    
    /**
     * Calculates the movment vector, that the distance is equal to 1, to avoid overspeeding, when moving diagonal and/or getting full vector to player
     */
    protected double[] getLimitedMovement(double[] movement) {
        double x = movement[0], 
               y = movement[1],
               distance = Math.sqrt(Math.pow(x,2) + Math.pow(y,2));
        if (distance <= 1) //returns if the distance is 1 or less.
            return movement;
        
        //calculates the multiplicator for x and y to get a distance of the added vectors, which is exaclty 1
        double r = Math.sqrt(Math.abs(1/(Math.pow(x,2)+Math.pow(y,2))));    
        //double r = Math.abs((Math.sqrt(Math.pow(x,2)*-1*Math.pow(y,2)+1)-x)/(Math.pow(y,2)+1));
        //System.out.println("r:"+r); //debug print statement
        return new double[] {x*r,y*r}; //returns the multiplied x and y values
    }
    
    /**
     * checks if cooldown is 0. resets cooldown to attackcooldown.
     */
    protected boolean checkCooldown() {
        boolean ret = currentcooldown == 0;
        if (ret) {
            currentcooldown = attackcooldown;
        }
        return ret;
    }
    //protected abstract void attack(Entity e);
    
    /**
     * sets the sprite to the given sprite. Includes propper scaling of the image to the overall game size
     */
    protected void setSprite(String spriteName, double scale) {
        GreenfootImage tmp = new GreenfootImage(spriteName); //fetches image
        //tmp.scale((int) ((DungeonWorld.pixelSize*DungeonWorld.pixelSize*scale)/tmp.getWidth()),(int) ((DungeonWorld.pixelSize*DungeonWorld.pixelSize*scale)/tmp.getHeight())); //scales it to pixelsize times scale
        tmp.scale((int) (tmp.getWidth()*scale*DungeonWorld.globalScale), (int) (tmp.getHeight()*scale*DungeonWorld.globalScale));
        setImage(tmp);//sets image
    }
    
    /**
     * calls setSprite(String spritename, double scale) using scale 1
     */
    protected void setSprite(String spriteName) {
        setSprite(spriteName, 1);
    }
}