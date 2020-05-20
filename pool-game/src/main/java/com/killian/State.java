package com.killian;

import java.util.ArrayList;
import java.util.List;

public class State {        
    private int playerIndex;
    private int firstHit;    
    private Stage stage;
    private boolean whiteHit;
    private boolean movement;
    private boolean ballBreak;
    private boolean firstBall;    
    private List<Integer> pocketedBalls;
    private List<Integer> pocketedFinal;
    private List<Billiard> BallsWallCollided;

    public State(){
        this.stage = Stage.INKITCHEN;
        this.playerIndex = 0;
        this.whiteHit = false;
        this.movement = false;
        this.ballBreak = true;
        this.firstBall = false;
        this.pocketedBalls = new ArrayList<Integer>();
        this.pocketedFinal = new ArrayList<Integer>();
        this.BallsWallCollided = new ArrayList<Billiard>();
    }

    public boolean getBreak(){
        return this.ballBreak;
    }

    public boolean getFirstBall(){
        return this.firstBall;
    }

    public boolean getMovement(){
        return this.movement;
    }

    public boolean getWhiteHit(){
        return this.whiteHit;
    }

    public int getPlayerIndex(){
        return this.playerIndex;
    }

    public int getFirstHit(){
        return this.firstHit;
    }

    public List<Billiard> getBallsWallCollided(){
        return this.BallsWallCollided;
    }

    public List<Integer> getPocketedBalls(){
        return this.pocketedBalls;
    }

    public List<Integer> getPocketedFinal(){
        return this.pocketedFinal;
    }

    public Stage getStage(){
        return this.stage;
    }

    public void setBreak(boolean flag){
        this.ballBreak = flag;
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void setMovement(Boolean movement){
        this.movement = movement;
    }

    public void setWhiteHit(boolean whiteHit){
        this.whiteHit = whiteHit;
    }

    public void setPlayerIndex(int playerIndex){
        this.playerIndex = playerIndex;
    }

    public void setFirstHit(int firstHit){
        this.firstHit = firstHit;
    }
}
