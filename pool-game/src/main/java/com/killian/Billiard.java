package com.killian;

/**
 * Billiard class to represent a billiard ball. Extends Ball.
 */
public class Billiard extends Ball {
    /**
     * Double X velocity value of billiard.
     */
    private double xVel;
    /**
     * Double Y velocity value of billiard.
     */
    private double yVel;
    /**
     * Boolean value of whether or not this billiard was the first ball the cue ball hit
     */
    private boolean firstHit;
    /**
     * Boolean value of whether or not this billiard has been potted.
     */
    private boolean pocketed;
    /**
     * Integer index value of this billiard in the array this billiard is to be stored.
     */
    private int index;
    
    /**
     * Constructor method for the Billiard class, assigns appropriate values to initial setting of the billiard.
     * @param x Double value of the initial X position of the billiard.
     * @param y Double value of the initial X position of the billiard.
     * @param ratio Double value of the ratio of this screen.
     * @param col String colour value the billiard is to be.
     * @param index Integer index value of this billiard in the parent array.
     */
    public Billiard(double x, double y, double ratio, String col, int index){
        super(x, y, 18.75*ratio, col);
        this.xVel = 0;
        this.yVel = 0;
        this.pocketed = false;
        this.firstHit = false;
        this.index = index;

    }

    /**
     * Method to get the index of this billiard.
     * @return
     */
    public int getIndex(){
        return this.index;
    }

    /**
     * Method to set whether or not this billiard was the first ball h it by the cue ball.
     * @param flag Boolean value to whether or not this billiard was the first ball h it by the cue ball..
     */
    public void setFirstHit(boolean flag){
        this.firstHit = flag;
    }

    /**
     * Method to get whether of not this billiard was the first ball to be hit by the cue ball.
     * @return Boolean value as to whether of not this billiard was the first ball to be hit by the cue ball.
     */
    public boolean getFirstHit(){
        return this.firstHit;
    }

    /**
     * Method to set the X velocity of this billiard.
     * @param xVel Double value to set this billiard's X velocity.
     */
    public void setXVel(double xVel){
        this.xVel = xVel;
    }

    /**
     * Method to set the Y velocity of this billiard.
     * @param xVel Double value to set this billiard's Y velocity.
     */
    public void setYVel(double yVel){
        this.yVel = yVel;
    }
    
    /**
     * Method to get the current X velocity of this billiard.
     * @return Double value of the current X velocity of this billiard.
     */
    public double getXVel(){
        return this.xVel;
    }

    /**
     * Method to get the current Y velocity of this billiard.
     * @return Double value of the current Y velocity of this billiard.
     */
    public double getYVel(){
        return this.yVel;
    }

    /**
     * Method to get whether or not this billiard has been potted yet.
     * @return Boolean value as to whether or not this billiard has been potted yet.
     */
    public boolean getPocketed(){
        return this.pocketed;
    }
}