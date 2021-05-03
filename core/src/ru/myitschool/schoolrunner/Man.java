package ru.myitschool.schoolrunner;

import com.badlogic.gdx.utils.TimeUtils;

public class Man {
    float x, y;
    float width, height;
    float dx, dy;
    int phase;
    static long timeLastPhase, timeInterval = 23;
    int condition;
    public static boolean isAlive = true;
    public static final int GO = 0;
    public static final int JUMP_UP = 1;
    public static final int JUMP_DOWN = 2;
    public static final int FAIL_CHAIR = 3;
    public static final int FAIL_DESK = 4;

    public Man(float x, float y) {
        this.x = x;
        this.y = y;
        width = MyGdxGame.SCR_HEIGHT/2.5f;
        height = MyGdxGame.SCR_HEIGHT/2.5f;
    }

    void move(){
        if(TimeUtils.millis() > timeLastPhase+timeInterval) {
            timeLastPhase = TimeUtils.millis();
            if (condition==GO) if (++phase == 20) phase = 0;
            if (condition==JUMP_UP){
                phase=0;
                y += 10;
                if(y > MyGdxGame.SCR_HEIGHT/1.8f) condition = JUMP_DOWN;
            }
            if (condition==JUMP_DOWN){
                y -= 10;
                if(y <= MyGdxGame.SCR_HEIGHT/5) condition = GO;
            }
            if (condition== FAIL_DESK){
                if(phase<=20) phase=21;
                if(++phase==30) phase=29;
                y=100;
                MyGdxGame.gameState = MyGdxGame.GAME_OVER;
            }
            if (condition == FAIL_CHAIR){
                if(phase<=20) phase=21;
                if(++phase==30) phase=29;
                y=100;
                MyGdxGame.gameState = MyGdxGame.GAME_OVER;
            }
        }
    }
}
