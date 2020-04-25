package com.killian;

public class Pool {
    private double ratio;
    private double diff;
    private GameArena gm;
    private Billiard[] billiards;

    public Pool() throws InterruptedException {
        ratio = 1.6;
        diff = 30 * ratio;
        //Class + Game Arena set up
               
        init();

        //Game loop
        boolean flag = false;
        while (flag) {
            update();
            render();
            Thread.sleep(10);
        }
    }

    public void init(){
        gm = new GameArena(1920, 1080);
        gm.addRectangle(new Rectangle(50, 50, 900 * ratio, 450 * ratio, "BROWN"));
        Table table = new Table((50 + diff), (50 + diff), (900*ratio - diff*2) , (450*ratio - diff*2));
        gm.addRectangle(table.getCloth());
        billiards = new Billiard[3]; 
        for(int i = 0; i < 3; i++){
            billiards[i] = new Billiard((i+1) * 200, (i+1) * 200, ratio);
            gm.addBall(billiards[i]);
        }
    }

    public void update(){
        
    }

    public void render(){
        
    }

    public void checkCollisions(){
        for(int i = 0; i < billiards.length; i++){
            for(int j = 0; j < billiards.length; j++){
                if(i!=j){
                    if(billiards[i].collides(billiards[j])){
                        // Collision between i & j
                    }
                }
            }
            
        }
    }

}