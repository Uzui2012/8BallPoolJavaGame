package com.killian;

public class Billiard extends Ball {
    private double xVel;
    private double yVel;
    private double xAcc;
    private double yAcc;
    private boolean firstHit;
    private boolean pocketed;
    
    public Billiard(double x, double y, double ratio, String col, int index){
        super(x, y, 18.75*ratio, col);
        this.xVel = 0;
        this.yVel = 0;
        this.xAcc = 0;
        this.yAcc = 0;
        this.pocketed = false;
        this.firstHit = false;

    }

    public void setFirstHit(boolean flag){
        this.firstHit = flag;
    }

    public boolean getFirstHit(){
        return this.firstHit;
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

    public void setXAcc(double xAcc){
        this.xAcc = xAcc;
    }

    public void setYAcc(double yAcc){
        this.yAcc = yAcc;
    }

    public double getXAcc(){
        return this.xAcc;
    }

    public double getYAcc(){
        return this.yAcc;
    }

    public boolean getPocketed(){
        return this.pocketed;
    }

    public void pocketed(State state){
        this.xVel = 0;
        this.yVel = 0;
        this.pocketed = true;
    }

}