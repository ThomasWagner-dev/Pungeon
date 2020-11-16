import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Superclass for all moving Objects.
 */
public abstract class Entity extends Actor
{
    protected int hp, maxhp, dmg, speed;
    protected String dmgType, type;
    
    /**
     * Yes
     */
    public void act() {
        move();
    }
    
    /**
     * moves the entity. If it is a Collider, it steps back elsewise colliding with an Object
     */
    protected void move() {
        int[] movementOriginal = getMovement();
        int[] movement = new int[] {movementOriginal[0]*speed, movementOriginal[1]*speed}; //Fetches movement
        
        int oldX = getX(),
            oldY = getY(),
            newX = oldX + movement[0],
            newY = oldY + movement[1];
        
        double workX = newX,
            workY = newY;
        //moves Entity to final location
        setLocation(newX, newY);
        
        //skips back movement if the entity doesn't have collision
        if (!(this instanceof Collider)) return;
        
        //checks goes back, until Entity ain't colliding anymore
        while (isTouching(Collider.class)) {
            if (workX == oldX && workY == oldY) 
                break;
            
            workX -= movementOriginal[0]; //Original movement to keep the ratio between x and y
            workY -= movementOriginal[1];
            setLocation((int) workX, (int) workY);
        }
        setLocation((int) workX, (int) workY);
    }
    
    /**
     * gets movement, dependent on either player input or AI
     */
    protected abstract int[] getMovement();
    
    /**
     * Basic collision physics. check act() in Projectile for further information
     */
    protected void collide(Projectile p) {
        DungeonWorld world = (DungeonWorld) getWorld();
        hp -= p.dmg * world.dmgMultiplier.get(p.dmgType).get(type);
        world.removeObject(p);
        if (hp < 0) {
            world.removeObject(this);
        }
        
    }
}
