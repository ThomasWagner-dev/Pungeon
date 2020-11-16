import greenfoot.*;
/**
 * Class to fetch inputs.
 * Required to make controlleraccessebility easier to add afterwards.
 * 
 * @author Commentator
 */
public class Inputs  
{
    /**
     * fetches momement multiplier.
     * 
     * @return movement multiplier as [x,y]
     */
    public static int[] getMovement() {
        int[] movement = new int[2];
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
        return movement;
    }
}
