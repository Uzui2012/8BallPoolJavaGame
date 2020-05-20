package com.killian;

public class Player
{
    private Colour colour;

    public Player(int index){
        this.colour = Colour.DEFAULT;
    }

    public Colour getColour(){
        return this.colour;
    }

    public void setColour(Colour col){
        this.colour = col;
    }

}