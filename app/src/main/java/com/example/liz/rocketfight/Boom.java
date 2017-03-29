package com.example.liz.rocketfight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by lenovo on 2017/3/29.
 */

public class Boom {
    private Bitmap bitmap;
    private int x;
    private int y;
    public Boom(Context context) {
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.boom);

        //将其放在视野之外，只在碰撞时使用它
        x = -400;
        y = -400;
    }

    //在碰撞时改变坐标使其可见
    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
