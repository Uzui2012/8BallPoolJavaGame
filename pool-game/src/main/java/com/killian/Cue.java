package com.killian;

/**
 * Cue class to represent the cue in the game of pool. Extends Line.
 */
public class Cue extends Line
{
    /**
     * Theta value of the angle off of the x-axis.
     */
    private double theta;
    /**
     * distance value of the distance between the cue and the cue ball.
     */
    private double distance;
    /**
     * Length of the cue.
     */
    private double length;
    /**
     * Ball radius value.
     */
    private double ballRad;

    /**
     * Cue constructor method, apppropriately assigns values and orientates the cue to a default position.
     * @param whiteX Double value of the cue ball's X position.
     * @param whiteY Double value of the cue ball's Y position.
     * @param ratio Double value of the ratio of the screen.
     */
    public Cue(double whiteX, double whiteY, double ratio){
        super(0, 0, 0, 0, 8, "GREY", 50);
        this.theta = 3.1416;
        this.distance = 20;
        this.length = 600;
        this.ballRad = 18.75*ratio;
        setCue(3.1416, whiteX, whiteY);        
    }

    /**
     * Method to set the position of the cue given a theta and cue ball position.
     * @param newTheta Double value of the new theta of the cue.
     * @param whiteX Double value of cue ball's X position.
     * @param whiteY Double value of cue ball's Y position.
     */
    public void setCue(double newTheta, double whiteX, double whiteY){
        this.theta = newTheta;
        setLinePosition((ballRad + distance) * Math.cos(theta) + whiteX,
                        (ballRad + distance) * Math.sin(theta) + whiteY,
                        (ballRad + distance + length) * Math.cos(theta) + whiteX,
                        (ballRad + distance + length) * Math.sin(theta) + whiteY);
    }

    /**
     * Method to get the current theta value of the cue off of the x-axis.
     * @return Double value of the current theta value of the cue off of the x-axis.
     */
    public double getTheta(){
        return this.theta;
    }

    /**
     * Method to set the visibility of the cue given a boolean value.
     * @param flag Boolean value to assign whether or not to display the cue.
     */
    public void setVisibiity(boolean flag){
        if(!flag){
            this.setWidth(0);
            setLinePosition((ballRad + distance) * Math.cos(theta) + 0,
                        (ballRad + distance) * Math.sin(theta) + 0,
                        (ballRad + distance + 0) * Math.cos(theta) + 0,
                        (ballRad + distance + 0) * Math.sin(theta) + 0);
        }else{
            this.setWidth(8);
        }
    }
}

