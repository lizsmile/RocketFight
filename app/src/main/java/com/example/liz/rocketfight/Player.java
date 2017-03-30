package com.example.liz.rocketfight;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

/**
 * Created by lenovo on 2017/3/29.
 */

public class Player {
    //获取角色的图片
    private Bitmap bitmap;
    private int x;
    private int y;
    private int speed = 0;
    private boolean boosting;
    private final int GRAVITY = -10;
    private int minY;
    private int maxY;
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 20;
    private Rect detectCollision;
    public Player(Context context,int screenX,int screenY){
        x = 75;
        y = 50;
        speed = -1;
        //加载位图文件
        bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.tray);
        boosting = false;
        detectCollision = new Rect(x,y,bitmap.getWidth(),bitmap.getHeight());
        maxY = screenY - bitmap.getHeight();
        minY = 0;
    }
    public void setBoosting(){
        boosting = true;
    }
    public void stopBoosting(){
        boosting = false;
    }
    public void update(){
        if(boosting){
            speed += 2;
        }else{
            speed -=5;
        }
        if(speed > MAX_SPEED){
            speed = MAX_SPEED;
        }
        if(speed < MIN_SPEED){
            speed = MIN_SPEED;
        }
        //加减号优先于自减
        y -= speed + GRAVITY;
        //防止跑到屏幕外
        if(y < minY){
            y = minY;
        }
        if(y > maxY){
            y = maxY;
        }
        detectCollision.left = x;
        detectCollision.top = y;
        detectCollision.right = x + bitmap.getWidth();
        detectCollision.bottom = y + bitmap.getHeight();
    }

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }
}
