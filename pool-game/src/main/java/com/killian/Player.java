package com.killian;

public class Player
{
    private Colour colour;
    private int index;

    public Player(int index){
        this.colour = Colour.DEFAULT;
        this.index = index;
    }

    public Colour getColour(){
        return this.colour;
    }

    public void setColour(Colour col){
        this.colour = col;
    }

    public void changePlayer(int currentIndex, Cue cue){
        if(this.index == 0){
            currentIndex = 1;
        }else{
            currentIndex = 0;
        }
    }
}