package ru.myitschool.schoolrunner;

public class Fon {
    float x, y;
    float width, height;
    static float dx=-1.2f;

    public Fon(float x, float y) {
        this.x = x;
        this.y = y;
        width = MyGdxGame.SCR_WIDTH;
        height = MyGdxGame.SCR_HEIGHT;
        //dx = -1.2f;
    }

    void move(){
        x += dx;
        if(x < - MyGdxGame.SCR_WIDTH) x = MyGdxGame.SCR_WIDTH;
    }
}

