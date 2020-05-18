package com.killian;

import java.util.ArrayList;
import java.util.List;

public class State {        
    private int playerIndex;
    private Stage stage;
    private boolean whiteHit;
    private boolean movement;
    private int firstHit;
    private List<Integer> pocketedBalls;
    private List<Integer> pocketedFinal;

    public State(Stage stage, int firstHit, int playerIndex, boolean whiteHit, boolean movement){
        this.stage = stage;
        this.firstHit = firstHit;
        this.playerIndex = playerIndex;
        this.whiteHit = whiteHit;
        this.movement = movement;
        this.pocketedBalls = new ArrayList<Integer>();
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

    public List<Integer> getPocketedBalls(){
        return this.pocketedBalls;
    }

    public List<Integer> getPocketedFinal(){
        return this.pocketedFinal;
    }

    public Stage getStage(){
        return this.stage;
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
