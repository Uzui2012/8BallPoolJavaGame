package com.killian;

/**
 * Class to represent a projection line. Extends Line.
 */
public class Projection extends Line
{
    /**
     * Projection constructor, assigns appropriate values of the object as a Line.
     */
    public Projection(double whiteX, double whiteY){
        super(0, 0, 0, 0, 1, "GREY");
    }

    /**
     * Method that sets the visibility of the projection line.
     * @param flag Boolean flag to state which intended visibility is wanted of this projection line.
     */
    public void setVisibiity(boolean flag){
        if(!flag){
            this.setWidth(0);
        }else{
            this.setWidth(2);
        }
    }
}