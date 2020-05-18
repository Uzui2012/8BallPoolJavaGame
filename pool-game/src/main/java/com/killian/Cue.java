package com.killian;

public class Cue extends Line
{
    private double theta;
    private double distance;
    private double length;
    private double ballRad;

    public Cue(double whiteX, double whiteY, double ratio){
        super(0, 0, 0, 0, 8, "GREY", 50);
        this.theta = 3.1416;
        this.distance = 20;
        this.length = 600;
        this.ballRad = 18.75*ratio;
        setCue(3.1416, whiteX, whiteY);        
    }

    public void setCue(double newTheta, double whiteX, double whiteY){
        this.theta = newTheta;
        setLinePosition((ballRad + distance) * Math.cos(theta) + whiteX,
                        (ballRad + distance) * Math.sin(theta) + whiteY,
                        (ballRad + distance + length) * Math.cos(theta) + whiteX,
                        (ballRad + distance + length) * Math.sin(theta) + whiteY);
    }

    public double getTheta(){
        return this.theta;
    }

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

