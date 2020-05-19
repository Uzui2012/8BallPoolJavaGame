package com.killian;

public class Pool 
{
    private final double ratio;
    private final double diff;
    private final double startOfCloth;
    private final double endOfClothX;
    private final double endOfClothY;
    private final double friction;
    
    private State state;
    private Pocket[] pockets;
    private Player[] players;
    private Billiard[] billiards;
    private Cue cue;
    private GameArena gm;

    public Pool() throws InterruptedException {
        this.ratio = 1.6;
        this.diff = 30 * ratio;
        this.startOfCloth = 150 + diff;
        this.endOfClothX = startOfCloth + 900 * ratio;
        this.endOfClothY = startOfCloth + 450 * ratio;
        this.friction = 0.9825;

        this.state = new State();
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

    private void update(){
        switch(this.state.getStage()){
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
        }      
    }

    private void calcRules(){
        if(this.state.getBreak()){
            //Break rules

        }

        
        if(!this.state.getPocketedBalls().isEmpty()){//IF NOT EMPTY
            if(this.state.getFirstHit() != 0 && billiards[this.state.getFirstHit()].getColour() != players[this.state.getPlayerIndex()].getColour().toString()){
                    this.state.getPocketedBalls().remove(this.state.getPocketedBalls().indexOf(0));
                    players[this.state.getPlayerIndex()].changePlayer(this.state.getPlayerIndex());
            }
        }else{

        }

        //Reset
        this.state.setStage(Stage.PLAYER);
        this.state.setWhiteHit(false);
        this.state.setFirstHit(0);
        this.state.getPocketedBalls().clear();

        System.out.println(this.state.getPocketedFinal());
        System.out.println(this.state.getPocketedBalls());
        System.out.println("Player; " + (this.state.getPlayerIndex()+1));
    }

    private void checkPocketed(){
        for(int i = 0; i < billiards.length; i++){
            for(Pocket p : pockets){
                double dx = billiards[i].getXPosition() - p.getXPosition();
		        double dy = billiards[i].getYPosition() - p.getYPosition();
		        double distance = Math.sqrt(dx*dx+dy*dy);
                if(distance < p.getSize()){
                    billiards[i].pocketed(this.state, i);
                    billiards[i].setXPosition(endOfClothX + 60*ratio);
                    billiards[i].setYPosition(startOfCloth + billiards[i].getSize()*(this.state.getPocketedFinal().size()-1));
                }
            }
        }
    }

    private void moveBalls(){
        checkBallCollision();
        checkWallCollision();
        for (Billiard i : billiards){
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
        checkPocketed();
        moveLogic();
    }

    private void moveLogic(){
        if(!this.state.getWhiteHit()){
            int i = 0;
            for(Billiard b : billiards){
                if(billiards[0].collides(b)){
                    this.state.setWhiteHit(true);;
                    this.state.setFirstHit(i);
                }
                i++;
            }
        }
        for(int i = 0; i < billiards.length; i++){
            if(billiards[i].getPocketed() && !this.state.getPocketedBalls().contains(i)){
                this.state.getPocketedBalls().add(i);
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

        double power = 15*ratio;
        if(gm.leftMousePressed()){
            billiards[0].setXVel(power*-Math.cos(cue.getTheta()));
            billiards[0].setYVel(power*-Math.sin(cue.getTheta()));
            this.state.setWhiteHit(false);
            this.state.setStage(Stage.MOVEMENT);
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
        if(!this.state.getWhiteHit()){
            for(Billiard b: billiards){
                if(billiards[0].collides(b) && !billiards[0].equals(b)){
                    this.state.setWhiteHit(true);
                    b.setFirstHit(true);
                }
            }
        }
    }

    private void checkWallCollision(){
        for(Billiard i : billiards){
            if(i.getXPosition() + i.getXVel() >= endOfClothX - i.getSize()/2){
                //Bounce off right wall
                i.setXVel(-i.getXVel() * friction);
            }else if(i.getXPosition() + i.getXVel() <= startOfCloth + i.getSize()/2){
                //Bounce off left wall
                i.setXVel(-i.getXVel() * friction);
            }else if(i.getYPosition() + i.getYVel() >= endOfClothY - i.getSize()/2){
                //Bounce off bottom wall
                i.setYVel(-i.getYVel() * friction);
            }else if(i.getYPosition() + i.getYVel() <= startOfCloth + i.getSize()/2){
                //Bounce off top wall
                i.setYVel(-i.getYVel() * friction);
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

    private void init(GameArena gm){
        players[0] = new Player(0);
        players[1] = new Player(1);
        
        double d = 18.75*ratio;
        double frontDotX = 750*ratio + startOfCloth;
        double frontDotY = 450*ratio/2 + startOfCloth;
        Table table = new Table(startOfCloth, startOfCloth, 900 * ratio, 450 * ratio);
        gm.addRectangle(new Rectangle(150, 150, 900 * ratio + diff * 2, 450 * ratio + diff * 2, "BROWN"));
        gm.addRectangle(table);

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

        Line baulkLine = new Line(startOfCloth + 210*ratio, startOfCloth, startOfCloth + 210*ratio, endOfClothY, 5, "WHITE");
        gm.addLine(baulkLine);

        billiards = new Billiard[16];
        billiards[0] = new Billiard(startOfCloth + 300, startOfCloth + 310, ratio, "WHITE", 0);
        billiards[1] = new Billiard(frontDotX - 0.5, frontDotY, ratio, "RED", 1);
        billiards[2] = new Billiard(frontDotX + d*Math.cos(0.523599), frontDotY + d*Math.sin(0.523599), ratio, "YELLOW", 2);
        billiards[3] = new Billiard(frontDotX + d*Math.cos(0.523599), frontDotY - d*Math.sin(0.523599), ratio, "RED", 3);
        billiards[4] = new Billiard(frontDotX + 2*d*Math.cos(0.523599) + 0.5, frontDotY, ratio, "BLACK", 4);
        billiards[5] = new Billiard(frontDotX + 2*d*Math.cos(0.523599) + 0.5, frontDotY + d, ratio, "RED", 5);
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

        this.cue = new Cue(billiards[0].getXPosition(), billiards[0].getYPosition(), ratio);
        gm.addLine(cue);
    }
}

