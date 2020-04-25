package com.killian;

public class Table {
    private Pocket[] pockets;
    private Rectangle cloth;

    public Table(double x, double y, double w, double h){
        this.pockets = new Pocket[6];
        cloth = new Rectangle(x, y, w, h, "DARKGREEN");
    }
    
    public Rectangle getCloth(){
        return this.cloth;
    }
}