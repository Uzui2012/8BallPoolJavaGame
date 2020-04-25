package com.killian;

public class Billiard extends Ball{
    private double xVel, yVel;

    public Billiard(double x, double y, double ratio){
        super(x, y, 18.75*ratio, "WHITE");
    }

    public void setXVel(double xVel){
        this.xVel = xVel;
    }

    public void setYVel(double yVel){
        this.yVel = yVel;
    }
    
    public double getXVel(){
        return this.xVel;
    }

    public double getYVel(){
        return this.yVel;
    }
}