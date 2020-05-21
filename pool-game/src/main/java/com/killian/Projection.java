package com.killian;

public class Projection extends Line
{
    public Projection(double whiteX, double whiteY, double ratio){
        super(0, 0, 0, 0, 1, "GREY", 51);
    }

    public void setVisibiity(boolean flag){
        if(!flag){
            this.setWidth(0);
        }else{
            this.setWidth(8);
        }
    }
}