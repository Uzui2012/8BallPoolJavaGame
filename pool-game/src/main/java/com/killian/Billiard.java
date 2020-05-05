package com.killian;

public class Billiard extends Ball{
    private double xVel;
    private double yVel;
    private double xAcc;
    private double yAcc;
    
    public Billiard(double x, double y, double ratio){
        super(x, y, 18.75*ratio, "WHITE");
        this.xVel = 0;
        this.yVel = 0;
        this.xAcc = 0;
        this.yAcc = 0;
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