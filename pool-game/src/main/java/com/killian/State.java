package com.killian;

import java.util.ArrayList;
import java.util.List;

/**
 * State class to represent the current state of the game.
 */
public class State {
    /**
     * Player index, used to point to the current player.
     */
    private int playerIndex;
    /**
     * Index to indicate which billiard has been hit during this shot.
     */
    private int firstHit;
    /**
     * Stage enum object to indicate current stage the game is in.
     */
    private Stage stage;
    /**
     * Boolean value to indicate whether or not the cue ball has hit any other ball.
     */
    private boolean whiteHit;
    /**
     * Boolean value to indicate whether or not any of the balls are moving currently.
     */
    private boolean movement;
    /**
     * Boolean value to indicate whether or not a pool break has occured.
     */
    private boolean ballBreak;
    /**
     * Boolean value to indicate whether or not a ball has been struck or not.
     */
    private boolean firstBall;
    /**
     * Integer List to keep track of potted balls during a turn.
     */
    private List<Integer> pocketedBalls;
    /**
     * Integer List to keep track of potted balls during a whole game.
     */
    private List<Integer> pocketedFinal;
    /**
     * Integer List to keep track of unpotted balls during a whole game.
     */
    private List<Integer> ballsInPlay;
    /**
     * Billiard List to keep track of balls that have collided with the wall.
     */
    private List<Billiard> BallsWallCollided;

    /**
     * State constructor, initialises variables, and adds all billiards to the unpotted List.
     * @param billiards Billirad[] array object that keeps track of all balls in the game.
     */
    public State(Billiard[] billiards){
        this.stage = Stage.INKITCHEN;
        this.playerIndex = 0;
        this.whiteHit = false;
        this.movement = false;
        this.ballBreak = true;
        this.firstBall = false;
        this.pocketedBalls = new ArrayList<Integer>();
        this.pocketedFinal = new ArrayList<Integer>();
        this.ballsInPlay = new ArrayList<Integer>();
        this.BallsWallCollided = new ArrayList<Billiard>();

        for(Billiard b : billiards){
            if(b.getIndex() != 0){
                ballsInPlay.add(b.getIndex());
            }
        }
    }

    /**
     * Method to get whether or not there has been a pool break.
     * @return Boolean value that indicates wheter or not there has been a pool break.
     */
    public boolean getBreak(){
        return this.ballBreak;
    }

    /**
     * Method to get whether or not a ball has been struck during this shot.
     * @return Boolean value whether or not a ball has been struck during this shot.
     */
    public boolean getFirstBall(){
        return this.firstBall;
    }

    /**
     * Method to get whether or not there is current movement by the billiards.
     * @return Boolean value whether or not there is current movement by the billiards.
    */
    public boolean getMovement(){
        return this.movement;
    }

    /**
     * Method to get whether or not the white ball has struck another ball.
     * @return Boolean value whether or not the white ball has struck another ball.s
     */
    public boolean getWhiteHit(){
        return this.whiteHit;
    }

    /**
     * Method to get the current player's index value.
     * @return Integer value of current player's index value.
     */
    public int getPlayerIndex(){
        return this.playerIndex;
    }

    /**
     * Method to get the first struck ball's index.
     * @return Integer value of the index of the first struck ball this turn.
     */
    public int getFirstHit(){
        return this.firstHit;
    }

    /**
     * Method to get the Billiard List of balls that collided with a wall this turn.
     * @return List<Billiard> object of balls that collided with a wall this turn.
     */
    public List<Billiard> getBallsWallCollided(){
        return this.BallsWallCollided;
    }

    /**
     * Method to get the Integer List of potted balls this turn.
     * @return List<Integer> object of potted balls this turn.
     */
    public List<Integer> getPocketedBalls(){
        return this.pocketedBalls;
    }

    /**
     * Method to get the Integer List of potted balls over the course of the whole game.
     * @return List<Integer> object of potted balls over the course of the whole game.
     */
    public List<Integer> getPocketedFinal(){
        return this.pocketedFinal;
    }

    /**
     * Method to get Integer List of balls currently in play at this moment.
     * @return List<Integer> object of balls currently in play at this moment.
     */
    public List<Integer> getBallsInPlay(){
        return this.ballsInPlay;
    }

    /**
     * Method to get the currect Stage of the game.
     * @return Stage enum object of the current state of the game.
     */
    public Stage getStage(){
        return this.stage;
    }

    /**
     * Method to set the boolean value of whether or not a pool break has occured.
     * @param flag Boolean value to set whether or not a pool break has occured.
     */
    public void setBreak(boolean flag){
        this.ballBreak = flag;
    }

    /**
     * Method to set the Stage value of the current state of the game.
     * @param stage Stage enum value to set the current state of the game.
     */
    public void setStage(Stage stage){
        this.stage = stage;
    }

    /**
     * Method to set the boolean value of whether or not there is currently movement of balls.
     * @param movement Boolean value of whether or not there is currently movement of balls.
     */
    public void setMovement(Boolean movement){
        this.movement = movement;
    }

    /**
     * Method to set the boolean value of whether or not the cue ball has hit an object ball.
     * @param whiteHit boolean value to set whether or not the cue ball has hit an object ball.
     */
    public void setWhiteHit(boolean whiteHit){
        this.whiteHit = whiteHit;
    }

    /**
     * Method to set the integer value of the current player index.
     * @param playerIndex Integer value of the newly set player index.
     */
    public void setPlayerIndex(int playerIndex){
        this.playerIndex = playerIndex;
    }

    /**
     * Method to set the integer value of the index of the first hit ball by the cue ball.
     * @param firstHit Integer value the index of the first hit ball by the cue ball.
     */
    public void setFirstHit(int firstHit){
        this.firstHit = firstHit;
    }
}
