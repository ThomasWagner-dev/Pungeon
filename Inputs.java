 

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
     * @return movement multiplier as [[x,y],[speedmultiplier]]
     */
    public static double[][] getMovement() {
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
        System.out.println(movement[0] + "," + movement[1]);
        return new double[][] {movement,new double[]{speedmultiplier}};
    }
}
