package com.killian;

public class Pool {
    private double ratio, diff, startOfCloth, endOfClothX, endOfClothY, friction;
    private GameArena gm;
    private Pocket[] pockets;
    private Billiard[] billiards;

    public Pool() throws InterruptedException {
        ratio = 1.5;
        diff = 30 * ratio;
        startOfCloth = 50 + diff;
        endOfClothX = startOfCloth + 900*ratio;
        endOfClothY = startOfCloth + 450*ratio;
        friction = 0.98;
        //Class + Game Arena set up               
        init();
        //Game Loop
        while(true){
            update(); 
            Thread.sleep(30); //Set FPS
        }        
    }

    private void update(){
        checkBallCollision();
        for(int i = 0; i < billiards.length; i++){
            billiards[i].setXVel(billiards[i].getXVel() * friction);
            billiards[i].setXPosition(billiards[i].getXPosition() + billiards[i].getXVel());
            billiards[i].setYVel(billiards[i].getYVel() * friction);
            billiards[i].setYPosition(billiards[i].getYPosition() + billiards[i].getYVel());
        }

    }

    private void init(){
        gm = new GameArena(1920, 1080);

        Table table = new Table(startOfCloth, startOfCloth, (900*ratio) , (450*ratio));
        gm.addRectangle(new Rectangle(50, 50, 900 * ratio + diff*2, 450 * ratio + diff*2, "BROWN"));        
        gm.addRectangle(table);

        pockets = new Pocket[6];
        pockets[0] = new Pocket(startOfCloth, startOfCloth, ratio);
        pockets[1] = new Pocket(endOfClothX, startOfCloth, ratio);
        pockets[2] = new Pocket((endOfClothX-startOfCloth)/2 + startOfCloth, startOfCloth, ratio);
        pockets[3] = new Pocket(startOfCloth, endOfClothY, ratio);
        pockets[4] = new Pocket(endOfClothX, endOfClothY, ratio);
        pockets[5] = new Pocket((endOfClothX-startOfCloth)/2 + startOfCloth, endOfClothY, ratio);
        for(int i = 0; i < pockets.length; i++){
            gm.addBall(pockets[i]);
        }

        billiards = new Billiard[3]; 
        for(int i = 0; i < 3; i++){
            billiards[i] = new Billiard((i+1) * 200, (i+1) * 200, ratio);
            gm.addBall(billiards[i]);
        }
    }    

    private void checkBallCollision(){
        for(int i = 0; i < billiards.length; i++){
            for(int j = 0; j < billiards.length; j++){
                if(i!=j){
                    if(billiards[i].collides(billiards[j])){
                        deflect(i, j);
                    }
                }
            }            
        }
    }

    private void deflect(int index1, int index2)
    {        
        // Calculate initial momentum of the balls... We assume unit mass here.
        double p1InitialMomentum = Math.sqrt(billiards[index1].getXVel() * billiards[index1].getXVel() + billiards[index1].getYVel() * billiards[index1].getYVel());
        double p2InitialMomentum = Math.sqrt(billiards[index2].getXVel() * billiards[index2].getXVel() + billiards[index2].getYVel() * billiards[index2].getYVel());
        // calculate motion vectors
        double[] p1Trajectory = {billiards[index1].getXVel(), billiards[index1].getYVel()};
        double[] p2Trajectory = {billiards[index2].getXVel(), billiards[index2].getYVel()};
        // Calculate Impact Vector
        double[] impactVector = {billiards[index2].getXPosition() - billiards[index1].getXPosition(), billiards[index2].getYPosition() - billiards[index1].getYPosition()};
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
        billiards[index1].setXVel(p1FinalTrajectory[0] * mag);
        billiards[index1].setYVel(p1FinalTrajectory[1] * mag);
        billiards[index2].setXVel(p2FinalTrajectory[0] * mag);
        billiards[index2].setYVel(p2FinalTrajectory[1] * mag);
    }

    private double[] normalizeVector(double[] vec)
    {
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

    private void checkWallCollision(){
        for(int i = 0; i < billiards.length; i++){
            if(billiards[i].getXPosition() >= endOfClothX - billiards[i].getSize()/2){
                //Bounce off right wall
            }else if(billiards[i].getXPosition() <= startOfCloth + billiards[i].getSize()/2){
                //Bounce off left wall
            }else if(billiards[i].getYPosition() >= endOfClothY - billiards[i].getSize()/2){
                //Bounce off bottom wall
            }else if(billiards[i].getYPosition() <= startOfCloth + billiards[i].getSize()/2){
                //Bounce off top wall
            }
        }
    }

}