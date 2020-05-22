package com.killian;

/**
 * Player class to represent the user(s) playing the game.
 */
public class Player
{
    /**
     * Colour object representing the current player's object ball colour
     */
    private Colour colour;

    /**
     * Player constructor, assigns deafult colour to player.
     */
    public Player(){
        this.colour = Colour.DEFAULT;
    }

    /**
     * Method to obtain this player's colour value.
     * @return This player's Colour enum value.
     */
    public Colour getColour(){
        return this.colour;
    }

    /**
     * Method to set this player's colour value.
     * @param col The intended Colour enum value of this player.
     */
    public void setColour(Colour col){
        this.colour = col;
    }

}