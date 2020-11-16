import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public abstract class Entity extends Actor
{
    protected int health, dmg, speed;
    protected String dmgType;
    public void act() {
        move();
    }
    protected void move() {
        int[] movement = getMovement(speed); //Fetches movement
        
        int oldX = getX(),
            oldY = getY(),
            newX = oldX + movement[0],
            newY = oldY + movement[1],
            workX = newX,
            workY = newY;
        //moves Entity to final location
        setLocation(newX, newY);
        
        //checks goes back, until Entity ain't colliding anymore
        while (isTouching(Collider.class)) {
            if (workX == oldX && workY == oldY) 
                break;
            
            workX -= Math.signum(movement[0]);
            workY -= Math.signum(movement[1]);
        }
        setLocation(workX, workY);
    }
    
    protected abstract int[] getMovement(); //AI dependent movements
    
    protected int[] getMovement(int speed) {
        int[] x = getMovement();
        return new int[] {x[0]*speed, x[1]*speed};
    }
}
