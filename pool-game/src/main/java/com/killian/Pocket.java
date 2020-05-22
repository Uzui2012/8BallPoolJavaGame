package com.killian;

/**
 * Pocket class to represent a pocket in the game of pool.
 */
public class Pocket extends Ball 
{
    /**
     * Constructor method for the Pocket class.
     * @param x Double value X coordinate to place the pocket.
     * @param y Double value Y coordinate to place the pocket.
     * @param ratio Double value of the ratio of the screen size.
     */
    public Pocket(double x, double y, double ratio) {
        super(x, y, 35*ratio, "BLACK");                
    }
}