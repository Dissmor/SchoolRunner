package ru.myitschool.schoolrunner;

public class Floor {
    float x, y;
    float width, height;
    static float dx=-3.6f;
    int type;

    public Floor(float x, int type) {
        this.x = x;
        this.type = type;
        width = MyGdxGame.SCR_WIDTH/5;
        height = MyGdxGame.SCR_HEIGHT/2.4f;
        //dx = -3.6f;
    }

    void move(){
        x += dx;
    }
}