package com.killian;

import javax.swing.JOptionPane;

import java.awt.event.*;

public class Pool extends WindowAdapter
{
    private final double ratio;
    private final double diff;
    private final double startOfCloth;
    private final double endOfClothX;
    private final double endOfClothY;
    private final double friction;
    private final Line baulkLine;
    
    private State state;
    private double power;
    private Rectangle powerBar;
    private Pocket[] pockets;
    private Player[] players;
    private Billiard[] billiards;
    private Cue cue;
    private Line wProjectionLine;
    private Line oProjectionLine;
    private Ball phantomBallOutLine;
    private Ball phantomBallFill;
    private Text playerStatus;
    private GameArena gm;

    public Pool() throws InterruptedException{
        this.ratio = 1.6;
        this.diff = 30 * ratio;
        this.startOfCloth = 150 + diff;
        this.endOfClothX = startOfCloth + 900 * ratio;
        this.endOfClothY = startOfCloth + 450 * ratio;
        this.friction = 0.9825;
        this.baulkLine = new Line(startOfCloth + 210*ratio, startOfCloth, startOfCloth + 210*ratio, endOfClothY, 5, "WHITE");        
        this.players = new Player[2]; //index 0: Player 1
                                      //index 1: Player 2
        this.gm = new GameArena(1920, 1080);
        // Class + Game Arena set up
        init(gm);
        // Game Loop
        while (true){
            update();
            Thread.sleep(6); // Set FPS
        }
    }

    private void update() throws InterruptedException{
        switch(this.state.getStage()){
            case INKITCHEN:
                getCueBallInKitchen();
                break;
            case INHAND:
                getCueBallInHand();
                break;
            case PLAYER:
                getInput();
                break;
            case MOVEMENT:
                moveBalls();
                if(!checkMovement()){
                    this.state.setStage(Stage.CALCULATE_RULES);
                }
                break;
            case CALCULATE_RULES:
                calcRules();
                break;
            case GAME_OVER:
                gameOver();
                break;
        }      
    }

    private void calcRules(){
        if(this.state.getBreak()){ //BREAK
            //Break logic
            if(this.state.getBallsWallCollided().size() >= 2 || (this.state.getPocketedBalls().size() > 0 && !this.state.getPocketedBalls().contains(0))){ 
                //SUCCESSFUL BREAK
                if(this.state.getPocketedBalls().size() == 0 ){
                    switchPlayers();
                }
                this.state.setBreak(false);
                this.state.setStage(Stage.PLAYER);
                if(this.state.getPocketedBalls().contains(Integer.valueOf(0))){
                    this.state.setStage(Stage.INKITCHEN);
                }
            }else{ 
                //FAILED BREAK
                switchPlayers();
                int ans = JOptionPane.showConfirmDialog(null, "Player " + (this.state.getPlayerIndex()+1) + " do you wish to re-rack?", "Unsuccessful Break", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);  
                if(ans == JOptionPane.YES_OPTION){
                    rerack();
                    this.state.setStage(Stage.INKITCHEN);
                }else{
                    this.state.setBreak(false);
                    this.state.setStage(Stage.PLAYER);
                }
                if(this.state.getPocketedBalls().contains(Integer.valueOf(0))){
                    this.state.setStage(Stage.INKITCHEN);
                }
                
            }
        }else{ //NORMAL SHOT
            switch(players[this.state.getPlayerIndex()].getColour()){
                case DEFAULT:
                    if(this.state.getFirstHit()!=0){ //HIT A BALL
                        if(this.state.getFirstHit() == 4){ //BLACK HIT
                            switchPlayers();
                            this.state.setStage(Stage.INHAND);
                            break;
                        }
                        if(this.state.getPocketedBalls().isEmpty()){ //EMPTY
                            switchPlayers();
                            this.state.setStage(Stage.PLAYER);
                        }else{
                            if(this.state.getPocketedBalls().contains(Integer.valueOf(0))){ //WHITE POTTED
                                this.state.setStage(Stage.INHAND);
                                switchPlayers();
                            }else if(this.state.getPocketedBalls().size() == 1){ //ONE
                                this.state.getBallsInPlay().remove(this.state.getPocketedBalls().get(0));
                                assignColours();
                                this.state.setStage(Stage.PLAYER);
                            }else{ //MORE THAN 1
                                int numRed = 0;
                                int numYel = 0;
                                for(int i : this.state.getPocketedBalls()){
                                    this.state.getBallsInPlay().remove(this.state.getPocketedBalls().get(i));
                                    if(billiards[i].getColour() == "RED"){
                                        numRed++;
                                    }else{
                                        numYel++;
                                    }
                                }
                                if(numRed !=0 & numYel != 0){ //DOUBLE COLOUR POTTED
                                    this.state.setStage(Stage.PLAYER);
                                }else{ //SINGLE COLOUR POTTED
                                    assignColours();
                                    this.state.setStage(Stage.PLAYER);
                                }
                            }
                        }
                    }else{ //HIT NOTHING
                        this.state.setStage(Stage.INHAND);
                        switchPlayers();
                    }
                    break;
                case RED:
                    if(billiards[this.state.getFirstHit()].getColour() == "RED"){ //HIT A RED
                        if(this.state.getPocketedBalls().isEmpty()){ //NOTHING POTTED
                            this.state.setStage(Stage.PLAYER);
                            switchPlayers();
                        }else{ //SOMETHING WAS POTTED
                            if(this.state.getPocketedBalls().contains(Integer.valueOf(0))){ //WHITE POTTED
                                this.state.setStage(Stage.INHAND);
                                switchPlayers();
                            }else if(this.state.getPocketedBalls().size() == 1){ //ONE POTTED
                                if(billiards[this.state.getPocketedBalls().get(0)].getColour() == "RED"){ //IT WAS RED
                                    this.state.setStage(Stage.PLAYER);
                                    this.state.getBallsInPlay().remove(this.state.getPocketedBalls().get(0));
                                }else{ //IT WASNT
                                    switchPlayers();
                                    this.state.getBallsInPlay().remove(this.state.getPocketedBalls().get(0));
                                    this.state.setStage(Stage.INHAND);
                                }                                
                            }else{ //MORE THAN 1
                                int numRed = 0;
                                for(int i : this.state.getPocketedBalls()){
                                    this.state.getBallsInPlay().remove(this.state.getPocketedBalls().get(0));
                                    if(billiards[i].getColour() == "RED"){
                                        numRed++;
                                    }
                                }
                                if(numRed > 0){ //POCKETED A RED
                                    this.state.setStage(Stage.PLAYER);
                                    int count = 0;
                                    for(int inPlay : this.state.getBallsInPlay()){
                                        if(billiards[inPlay].getColour() == "RED")
                                        {
                                            count++;
                                        }
                                    }
                                    if(count == 0){
                                        players[this.state.getPlayerIndex()].setColour(Colour.BLACK);
                                    }
                                }else{ //DIDNT
                                    switchPlayers();
                                    this.state.setStage(Stage.INHAND);
                                }
                            }
                        }
                    }else{ //DIDNT HIT A RED
                        this.state.setStage(Stage.INHAND);
                        switchPlayers();
                    }
                    break;
                case YELLOW:
                    if(billiards[this.state.getFirstHit()].getColour() == "YELLOW"){ //HIT A YELLOW
                        if(this.state.getPocketedBalls().isEmpty()){ //NOTHING POTTED
                            this.state.setStage(Stage.PLAYER);
                            switchPlayers();
                        }else{ //SOMETHING WAS POTTED
                            if(this.state.getPocketedBalls().contains(Integer.valueOf(0))){ //WHITE POTTED
                                this.state.setStage(Stage.INHAND);
                                switchPlayers();
                            }else if(this.state.getPocketedBalls().size() == 1){ //ONE POTTED
                                if(billiards[this.state.getPocketedBalls().get(0)].getColour() == "YELLOW"){ //IT WAS YELLOW
                                    this.state.setStage(Stage.PLAYER);
                                    this.state.getBallsInPlay().remove(this.state.getPocketedBalls().get(0));
                                }else{ //IT WASNT
                                    switchPlayers();
                                    this.state.getBallsInPlay().remove(this.state.getPocketedBalls().get(0));
                                    this.state.setStage(Stage.INHAND);
                                }                                
                            }else{ //MORE THAN 1
                                int numYel = 0;
                                for(int i : this.state.getPocketedBalls()){
                                    this.state.getBallsInPlay().remove(this.state.getPocketedBalls().get(0));
                                    if(billiards[i].getColour() == "YELLOW"){
                                        numYel++;
                                    }
                                }
                                if(numYel > 0){ //POCKETED A YELLOW
                                    this.state.setStage(Stage.PLAYER);
                                }else{ //DIDNT
                                    switchPlayers();
                                    this.state.setStage(Stage.INHAND);
                                }
                            }
                        }
                    }else{ //DIDNT HIT A YELLOW
                        this.state.setStage(Stage.INHAND);
                        switchPlayers();
                    }
                    break;
                case BLACK:
                    if(this.state.getFirstHit() == 4){ //HIT THE BLACK
                        if(this.state.getPocketedBalls().isEmpty()){ //NOTHING POTTED
                            this.state.setStage(Stage.PLAYER);
                            switchPlayers();
                        }else{ //SOMETHING WAS POTTED
                            if(this.state.getPocketedBalls().size() == 1){ //ONE POTTED
                                if(billiards[this.state.getPocketedBalls().get(0)].getColour() == "BLACK"){ //IT WAS BLACK
                                    this.state.setStage(Stage.GAME_OVER);
                                }else{ //IT WASNT
                                    switchPlayers();
                                    this.state.setStage(Stage.PLAYER);
                                    if(this.state.getPocketedBalls().contains(Integer.valueOf(0))){ //IT WAS WHITE
                                        this.state.setStage(Stage.INHAND);
                                    }
                                }                                
                            }else{ //MORE THAN 1
                                if(this.state.getPocketedBalls().contains(4)){ //AT LEAST POTTED BLACK
                                    this.state.setStage(Stage.GAME_OVER);
                                    if(this.state.getPocketedBalls().contains(Integer.valueOf(0))){ //POTTED BLACK & WHITE
                                        //LOSE
                                        switchPlayers();
                                        this.state.setStage(Stage.GAME_OVER);
                                    }
                                }else{ //NO BLACK POTTED
                                    switchPlayers();
                                    this.state.setStage(Stage.PLAYER);
                                    if(this.state.getPocketedBalls().contains(Integer.valueOf(0))){
                                        this.state.setStage(Stage.INHAND);
                                    }
                                }

                            }
                        }
                    }else{ //DIDNT HIT A BLACK
                        this.state.setStage(Stage.INHAND);
                        switchPlayers();
                    }
                    break;
            }
        }
        if(players[this.state.getPlayerIndex()].getColour() == Colour.RED){
            int count = 0;
            for(int inPlay : this.state.getBallsInPlay()){
                if(billiards[inPlay].getColour() == "RED")
                {
                    count++;
                }
            }
            if(count == 0){
                players[this.state.getPlayerIndex()].setColour(Colour.BLACK);
            }
        }else if(players[this.state.getPlayerIndex()].getColour() == Colour.YELLOW){
            int count = 0;
            for(int inPlay : this.state.getBallsInPlay()){
                if(billiards[inPlay].getColour() == "YELLOW")
                {
                    count++;
                }
            }
            if(count == 0){
                players[this.state.getPlayerIndex()].setColour(Colour.BLACK);
            }
        }
        this.state.setWhiteHit(false);
        this.state.setFirstHit(0);
        this.state.getPocketedBalls().clear();
        this.state.getPocketedFinal().remove(Integer.valueOf(0));
        this.playerStatus.setText("Player:" + (this.state.getPlayerIndex()+1) + "    Colour: " + players[this.state.getPlayerIndex()].getColour());
    }

    private void gameOver(){
        gm.clearGameArena();
        gm.addText(new Text("Player " + (this.state.getPlayerIndex()+1) + " HAS WON!", 45, 500, 500, "WHITE"));
    }

    private void assignColours(){
        if(billiards[this.state.getPocketedBalls().get(Integer.valueOf(0))].getColour() == "RED"){
            players[this.state.getPlayerIndex()].setColour(Colour.RED);
            switchPlayers();
            players[this.state.getPlayerIndex()].setColour(Colour.YELLOW);
            switchPlayers();
        }else{
            players[this.state.getPlayerIndex()].setColour(Colour.YELLOW);
            switchPlayers();
            players[this.state.getPlayerIndex()].setColour(Colour.RED);
            switchPlayers();
        }
        
    }

    private void switchPlayers(){
        if(this.state.getPlayerIndex() == 0){
            this.state.setPlayerIndex(1);
            if(players[1].getColour() != Colour.DEFAULT){
                this.cue.setColour(players[1].getColour().toString());
            }
        }else{
            this.state.setPlayerIndex(0);
            if(players[0].getColour() != Colour.DEFAULT){
                this.cue.setColour(players[0].getColour().toString());
            }
        }
    }

    private void checkPocketed(){
        for(Billiard b : billiards){
            for(Pocket p : pockets){
                double dx = b.getXPosition() - p.getXPosition();
		        double dy = b.getYPosition() - p.getYPosition();
                double distance = Math.sqrt(dx*dx+dy*dy);
                if(distance < p.getSize() && !b.getPocketed()){
                    b.setXVel(0);
                    b.setYVel(0);
                    b.setXPosition(endOfClothX + 60*ratio);
                    this.state.getPocketedBalls().add(b.getIndex());
                    this.state.getPocketedFinal().add(b.getIndex());
                    b.setYPosition(startOfCloth + b.getSize()*this.state.getPocketedFinal().size());                   
                }
            }
        }
    }

    private void moveBalls(){
        checkBallCollision();
        checkWallCollision();        
        checkPocketed();
        for (Billiard i : billiards){
            if(!i.getPocketed()){
                i.setXVel(i.getXVel() * friction);
                i.setXPosition(i.getXPosition() + i.getXVel());
                i.setYVel(i.getYVel() * friction);
                i.setYPosition(i.getYPosition() + i.getYVel());
                if(i.getXVel()*i.getXVel() < 0.001){
                    i.setXVel(0);
                }
                if(i.getYVel()*i.getYVel() < 0.001){
                    i.setYVel(0);
                }
            }           
        }
    }
    
    private void getInput(){
        //Calculate mouse to white ball direction vector
        double mouseX = gm.getMousePositionX();
        double mouseY = gm.getMousePositionY();
        double[] mouseWhiteVec = {mouseX - billiards[0].getXPosition(), billiards[0].getYPosition() - mouseY}; //{xDir,yDir}
        double arcTan= Math.atan(mouseWhiteVec[1]/mouseWhiteVec[0]);
        double theta;
        if(mouseWhiteVec[0] > 0){
            theta = -arcTan;
        }else{
            theta = Math.PI - arcTan;
        }
        //Set cue rotation to mouse to white ball direciton vector
        cue.setCue(theta, billiards[0].getXPosition(), billiards[0].getYPosition());        
        double[] endProj = calcProj(theta);
        wProjectionLine.setWidth(2);
        wProjectionLine.setLinePosition(billiards[0].getXPosition(), billiards[0].getYPosition(), endProj[0], endProj[1]);
        if(gm.leftMousePressed()){
            billiards[0].setXVel(power*-Math.cos(cue.getTheta()));
            billiards[0].setYVel(power*-Math.sin(cue.getTheta()));
            this.state.setWhiteHit(false);
            this.state.setStage(Stage.MOVEMENT);
        }else if(gm.upPressed() && power <= 60){
            this.powerBar.setWidth(powerBar.getWidth() + 8);
            power++;
        }else if(gm.downPressed() && power > 1){
            this.powerBar.setWidth(powerBar.getWidth() - 8);
            power--;                        
        }
    }

    private double[] calcProj(double theta){
        double newTheta = theta;
        phantomBallOutLine.setColour("BLACK");
        phantomBallOutLine.setXPosition(0);
        phantomBallOutLine.setYPosition(0);
        phantomBallFill.setColour("BLACK");
        phantomBallFill.setXPosition(0);
        phantomBallFill.setYPosition(0);
        double[] startPos = {billiards[0].getXPosition(), billiards[0].getYPosition()};
        double[] endPos = startPos;
        //CAST RAY IN DIRECTION THETA        
        for(int i = 0; i < 110000; i ++){
            endPos[0] = endPos[0] - 0.01*Math.cos(newTheta);
            endPos[1] = endPos[1] - 0.01*Math.sin(newTheta);
            for(int j = 1; j < billiards.length; j++){
                double distance = Math.sqrt((endPos[0] - billiards[j].getXPosition())*(endPos[0] - billiards[j].getXPosition()) + 
                                            (endPos[1] - billiards[j].getYPosition())*(endPos[1] - billiards[j].getYPosition()));
                if(distance < billiards[j].getSize()){ //IF THIS RAY HITS ANY BALL
                    phantomBallOutLine.setColour("GREY"); //PROJECT PHANTOM BALL
                    phantomBallOutLine.setXPosition(endPos[0]);
                    phantomBallOutLine.setYPosition(endPos[1]);
                    phantomBallFill.setColour("DARKGREEN");
                    phantomBallFill.setXPosition(endPos[0]);
                    phantomBallFill.setYPosition(endPos[1]);
                    i = 110000; //HALT CASTING RAY
                }
            }
        }        
        return endPos;
    }

    private void getCueBallInHand() throws InterruptedException{
        cue.setVisibiity(false);
        double mouseX = gm.getMousePositionX();
        double mouseY = gm.getMousePositionY();
        if(mouseX < endOfClothX-billiards[0].getSize()/2 && mouseX > startOfCloth+billiards[0].getSize()/2 &&
           mouseY < endOfClothY-billiards[0].getSize()/2 && mouseY > startOfCloth+billiards[0].getSize()/2){
            billiards[0].setXPosition(mouseX);
            billiards[0].setYPosition(mouseY);
            if(gm.leftMousePressed()){
                billiards[0].setXPosition(mouseX);
                billiards[0].setYPosition(mouseY);
                this.state.setStage(Stage.PLAYER);
                Thread.sleep(200);
                cue.setVisibiity(true);
            }
        }        
    }

    private void getCueBallInKitchen() throws InterruptedException{
        cue.setVisibiity(false);
        double mouseX = gm.getMousePositionX();
        double mouseY = gm.getMousePositionY();
        if(mouseX < this.baulkLine.getXStart() && mouseX > startOfCloth+billiards[0].getSize()/2 && mouseY < endOfClothY-billiards[0].getSize()/2 && mouseY > startOfCloth+billiards[0].getSize()/2){
            billiards[0].setXPosition(mouseX);
            billiards[0].setYPosition(mouseY);
            if(gm.leftMousePressed()){
                billiards[0].setXPosition(mouseX);
                billiards[0].setYPosition(mouseY);
                this.state.setStage(Stage.PLAYER);
                Thread.sleep(200);
                cue.setVisibiity(true);
            }
        }        
    }

    private boolean checkMovement(){
        boolean flag = false;
        for(Billiard i : billiards){
            if(i.getXVel() == 0 && i.getYVel() == 0){
                flag = false;
            }else{
                flag = true;
                break;
            }
        }
        return flag;
    }

    private void checkBallCollision(){
        for(Billiard i : billiards){
            for(Billiard j : billiards){
                if(!i.equals(j) && i.collides(j)){
                        deflect(i, j);
                }                
            }
        }
        if(!this.state.getWhiteHit()){ // NOT: Has white hit another ball ? 
            for(Billiard b: billiards){
                if(billiards[0].collides(b) && b.getIndex() != 0){
                    this.state.setWhiteHit(true);
                    b.setFirstHit(true);
                    this.state.setFirstHit(b.getIndex());
                }
            }
        }
    }

    private void checkWallCollision(){
        for(Billiard i : billiards){
            if(i.getXPosition() + i.getXVel() >= endOfClothX - i.getSize()/2){
                //Bounce off right wall
                i.setXVel(-i.getXVel() * friction);
                if(!this.state.getBallsWallCollided().contains(i) && i.getIndex() != 0){
                    this.state.getBallsWallCollided().add(i);
                }
            }else if(i.getXPosition() + i.getXVel() <= startOfCloth + i.getSize()/2){
                //Bounce off left wall
                i.setXVel(-i.getXVel() * friction);
                if(!this.state.getBallsWallCollided().contains(i) && i.getIndex() != 0){
                    this.state.getBallsWallCollided().add(i);
                }
            }else if(i.getYPosition() + i.getYVel() >= endOfClothY - i.getSize()/2){
                //Bounce off bottom wall
                i.setYVel(-i.getYVel() * friction);
                if(!this.state.getBallsWallCollided().contains(i) && i.getIndex() != 0){
                    this.state.getBallsWallCollided().add(i);
                }
            }else if(i.getYPosition() + i.getYVel() <= startOfCloth + i.getSize()/2){
                //Bounce off top wall
                i.setYVel(-i.getYVel() * friction);
                if(!this.state.getBallsWallCollided().contains(i) && i.getIndex() != 0){
                    this.state.getBallsWallCollided().add(i);
                }
            }
        }
    }

    private void deflect(Billiard billiard1, Billiard billiard2){        
        // Calculate initial momentum of the balls... We assume unit mass here.
        double p1InitialMomentum = Math.sqrt(billiard1.getXVel() * billiard1.getXVel() + billiard1.getYVel() * billiard1.getYVel());
        double p2InitialMomentum = Math.sqrt(billiard2.getXVel() * billiard2.getXVel() + billiard2.getYVel() * billiard2.getYVel());
        // calculate motion vectors
        double[] p1Trajectory = {billiard1.getXVel(), billiard1.getYVel()};
        double[] p2Trajectory = {billiard2.getXVel(), billiard2.getYVel()};
        // Calculate Impact Vector
        double[] impactVector = {billiard2.getXPosition() - billiard1.getXPosition(), billiard2.getYPosition() - billiard1.getYPosition()};
        double[] impactVectorNorm = normalizeVector(impactVector);
        // Calculate scalar product of each trajectory and impact vector
        double p1dotImpact = Math.abs(p1Trajectory[0] * impactVectorNorm[0] + p1Trajectory[1] * impactVectorNorm[1]);
        double p2dotImpact = Math.abs(p2Trajectory[0] * impactVectorNorm[0] + p2Trajectory[1] * impactVectorNorm[1]);
        // Calculate the deflection vectors - the amount of energy transferred from one ball to the other in each axis
        double[] p1Deflect = { -impactVectorNorm[0] * p2dotImpact, -impactVectorNorm[1] * p2dotImpact };
        double[] p2Deflect = { impactVectorNorm[0] * p1dotImpact, impactVectorNorm[1] * p1dotImpact };
        // Calculate the final trajectories
        double[] p1FinalTrajectory = {p1Trajectory[0] + p1Deflect[0] - p2Deflect[0], p1Trajectory[1] + p1Deflect[1] - p2Deflect[1]};
        double[] p2FinalTrajectory = {p2Trajectory[0] + p2Deflect[0] - p1Deflect[0], p2Trajectory[1] + p2Deflect[1] - p1Deflect[1]};
        // Calculate the final energy in the system.
        double p1FinalMomentum = Math.sqrt(p1FinalTrajectory[0] * p1FinalTrajectory[0] + p1FinalTrajectory[1] * p1FinalTrajectory[1]);
        double p2FinalMomentum = Math.sqrt(p2FinalTrajectory[0] * p2FinalTrajectory[0] + p2FinalTrajectory[1] * p2FinalTrajectory[1]);
        // Scale the resultant trajectories if we've accidentally broken the laws of physics.
        double mag = (p1InitialMomentum + p2InitialMomentum) / (p1FinalMomentum + p2FinalMomentum);
        // Calculate the final x and y speed settings for the two balls after collision.
        billiard1.setXVel(p1FinalTrajectory[0] * mag);
        billiard1.setYVel(p1FinalTrajectory[1] * mag);
        billiard2.setXVel(p2FinalTrajectory[0] * mag);
        billiard2.setYVel(p2FinalTrajectory[1] * mag);
    }

    private double[] normalizeVector(double[] vec){
        double mag = 0.0;
        int dimensions = vec.length;
        double[] result = new double[dimensions];
        for (int i=0; i < dimensions; i++){
            mag += vec[i] * vec[i];
        }
        mag = Math.sqrt(mag);
        if (mag == 0.0){
            result[0] = 1.0;
            for (int i=1; i < dimensions; i++){
                result[i] = 0.0;
            }
        }
        else{
            for (int i=0; i < dimensions; i++){
                result[i] = vec[i] / mag;
            }
        }
        return result;
    }

    private void rerack(){
        double d = 18.75*ratio;
        double frontDotX = 750*ratio + startOfCloth;
        double frontDotY = 450*ratio/2 + startOfCloth;
        billiards[1].setXPosition(frontDotX - 0.5);
        billiards[1].setYPosition(frontDotY);
        billiards[2].setXPosition(frontDotX + d*Math.cos(0.523599));
        billiards[2].setYPosition(frontDotY + d*Math.sin(0.523599));
        billiards[3].setXPosition(frontDotX + d*Math.cos(0.523599));
        billiards[3].setYPosition(frontDotY - d*Math.sin(0.523599));
        billiards[4].setXPosition(frontDotX + 2*d*Math.cos(0.523599) + 0.5);
        billiards[4].setYPosition(frontDotY);
        billiards[5].setXPosition(frontDotX + 2*d*Math.cos(0.523599) + 0.5);
        billiards[5].setYPosition(frontDotY + d);
        billiards[6].setXPosition(frontDotX + 2*d*Math.cos(0.523599) + 0.5);
        billiards[6].setYPosition(frontDotY - d);
        billiards[7].setXPosition(frontDotX + 3*d*Math.cos(0.523599) + 1);
        billiards[7].setYPosition(frontDotY + d*Math.sin(0.523599));
        billiards[8].setXPosition(frontDotX + 3*d*Math.cos(0.523599) + 1);
        billiards[8].setYPosition(frontDotY - d*Math.sin(0.523599));
        billiards[9].setXPosition(frontDotX + 3*d*Math.cos(0.523599) + 1);
        billiards[9].setYPosition(frontDotY + d*Math.sin(0.523599) + d);
        billiards[10].setXPosition(frontDotX + 3*d*Math.cos(0.523599) + 1);
        billiards[10].setYPosition(frontDotY - d*Math.sin(0.523599) - d);
        billiards[11].setXPosition(frontDotX + 4*d*Math.cos(0.523599) + 1.5);
        billiards[11].setYPosition(frontDotY);
        billiards[12].setXPosition(frontDotX + 4*d*Math.cos(0.523599) + 1.5);
        billiards[12].setYPosition(frontDotY + d);
        billiards[13].setXPosition(frontDotX + 4*d*Math.cos(0.523599) + 1.5);
        billiards[13].setYPosition(frontDotY - d);
        billiards[14].setXPosition(frontDotX + 4*d*Math.cos(0.523599) + 1.5);
        billiards[14].setYPosition(frontDotY + 2*d);
        billiards[15].setXPosition(frontDotX + 4*d*Math.cos(0.523599) + 1.5);
        billiards[15].setYPosition(frontDotY - 2*d);
    }

    private void init(GameArena gm){
        players[0] = new Player();
        players[1] = new Player();
        power = 30;        
        double d = 18.75*ratio;
        double frontDotX = 750*ratio + startOfCloth;
        double frontDotY = 450*ratio/2 + startOfCloth;
        Table table = new Table(startOfCloth, startOfCloth, 900 * ratio, 450 * ratio);
        gm.addRectangle(new Rectangle(150, 150, 900 * ratio + diff * 2, 450 * ratio + diff * 2, "BROWN"));
        gm.addRectangle(table);
        Text pbText = new Text("Power Bar:", 20, startOfCloth, startOfCloth - 100, "WHITE");
        gm.addText(pbText);
        powerBar = new Rectangle(startOfCloth, startOfCloth - 80, 250, 30, "BLUE");        
        gm.addRectangle(powerBar);
        pockets = new Pocket[6];
        pockets[0] = new Pocket(startOfCloth, startOfCloth, ratio);
        pockets[1] = new Pocket(endOfClothX, startOfCloth, ratio);
        pockets[2] = new Pocket((endOfClothX - startOfCloth) / 2 + startOfCloth, startOfCloth - 10, ratio);
        pockets[3] = new Pocket(startOfCloth, endOfClothY, ratio);
        pockets[4] = new Pocket(endOfClothX, endOfClothY, ratio);
        pockets[5] = new Pocket((endOfClothX - startOfCloth) / 2 + startOfCloth, endOfClothY + 10, ratio);
        for (Pocket pocket : pockets){
            gm.addBall(pocket);
        }
        gm.addLine(baulkLine);
        billiards = new Billiard[16];
        billiards[0] = new Billiard(startOfCloth + 300, startOfCloth + 310, ratio, "WHITE", 0);
        billiards[1] = new Billiard(frontDotX - 0.5, frontDotY, ratio, "RED", 1);
        billiards[2] = new Billiard(frontDotX + d*Math.cos(0.523599), frontDotY + d*Math.sin(0.523599), ratio, "YELLOW", 2);
        billiards[3] = new Billiard(frontDotX + d*Math.cos(0.523599), frontDotY - d*Math.sin(0.523599), ratio, "RED", 3);
        billiards[4] = new Billiard(frontDotX + 2*d*Math.cos(0.523599) + 0.5, frontDotY, ratio, "BLACK", 4);
        billiards[5] = new Billiard(frontDotX + 2*d*Math.cos(0.523599) + 0.5, frontDotY + d, ratio, "YELLOW", 5);
        billiards[6] = new Billiard(frontDotX + 2*d*Math.cos(0.523599) + 0.5, frontDotY - d, ratio, "YELLOW", 6);
        billiards[7] = new Billiard(frontDotX + 3*d*Math.cos(0.523599) + 1 , frontDotY + d*Math.sin(0.523599), ratio, "RED", 7);
        billiards[8] = new Billiard(frontDotX + 3*d*Math.cos(0.523599) + 1, frontDotY - d*Math.sin(0.523599), ratio, "YELLOW", 8);
        billiards[9] = new Billiard(frontDotX + 3*d*Math.cos(0.523599) + 1, frontDotY + d*Math.sin(0.523599) + d, ratio, "YELLOW", 9);
        billiards[10] = new Billiard(frontDotX + 3*d*Math.cos(0.523599) + 1, frontDotY - d*Math.sin(0.523599) - d, ratio, "RED", 10);
        billiards[11] = new Billiard(frontDotX + 4*d*Math.cos(0.523599) + 1.5, frontDotY, ratio, "RED", 11);
        billiards[12] = new Billiard(frontDotX + 4*d*Math.cos(0.523599) + 1.5, frontDotY + d, ratio, "YELLOW", 12);
        billiards[13] = new Billiard(frontDotX + 4*d*Math.cos(0.523599) + 1.5, frontDotY - d, ratio, "YELLOW", 13);
        billiards[14] = new Billiard(frontDotX + 4*d*Math.cos(0.523599) + 1.5, frontDotY + 2*d, ratio, "RED", 14);
        billiards[15] = new Billiard(frontDotX + 4*d*Math.cos(0.523599) + 1.5, frontDotY - 2*d, ratio, "YELLOW", 15);
        for (int i = 0; i < 16; i ++){
            gm.addBall(billiards[i]);
        }
        this.state = new State(billiards);
        playerStatus = new Text("Player:" + (this.state.getPlayerIndex()+1) + "    Colour: " + players[this.state.getPlayerIndex()].getColour(), 20, startOfCloth + 700, startOfCloth - 100, "WHITE");
        gm.addText(playerStatus);
        phantomBallOutLine = new Ball(0, 0, billiards[0].getSize(), "GREY");
        phantomBallFill = new Ball(0, 0, billiards[0].getSize() - 3, "GREY");
        gm.addBall(phantomBallOutLine);
        gm.addBall(phantomBallFill);
        this.cue = new Cue(billiards[0].getXPosition(), billiards[0].getYPosition(), ratio);
        this.wProjectionLine = new Line(0, 0, 0, 0, 1, "GREY");
        this.oProjectionLine = new Line(0, 0, 0, 0, 1, "GREY");
        gm.addLine(cue);
        gm.addLine(wProjectionLine);
        gm.addLine(oProjectionLine);
    }
}

