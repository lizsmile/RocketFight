package com.example.liz.rocketfight;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

/**
 * Created by lenovo on 2017/3/29.
 */

public class GameView extends SurfaceView implements Runnable {
    //记录游戏是否是在进行
    volatile boolean playing;
    private Thread gameThread = null;
    private Player player;
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder surfaceHolder;
    private Enemy[] enemies;
    //设置敌人的数量
    private int enemyCount = 1;
    private Friend friend;
    private ArrayList<Star> stars = new ArrayList<Star>();
    private Boom boom;
    int screenX;
    int countMisses;
    boolean flag;
    private boolean isGameOver;
    int score;
    //存放4个分数的数组
    int highScore[] = new int[5];
    //将分数存在shared preference
    SharedPreferences sharedPreferences;
    static MediaPlayer gameOnSound;
    final MediaPlayer killedEnemysound;
    final MediaPlayer gameOversound;
    //当在游戏结束触摸时，返回游戏开始页面
    Context context;
    public GameView(Context context,int screenX,int screenY){
        super(context);
        player = new Player(context,screenX,screenY);
        surfaceHolder = getHolder();
        paint = new Paint();
        //向数组中添加星星
        int starNums = 100;
        for (int i = 0; i< starNums; i++){
            Star s = new Star(screenX,screenY);
            stars.add(s);
        }
        //初始化敌人数组
        enemies = new Enemy[enemyCount];
        for (int i = 0; i < enemyCount; i++){
            enemies[i] = new Enemy(context,screenX,screenY);
        }
        boom = new Boom(context);
        friend = new Friend(context, screenX, screenY);
        this.screenX = screenX;
        countMisses = 0;
        isGameOver = false;
        score = 0;
        sharedPreferences = context.getSharedPreferences("SHAR_PREF_NAME",Context.MODE_PRIVATE);
        //初始化分数
        highScore[0] = sharedPreferences.getInt("score1",0);
        highScore[1] = sharedPreferences.getInt("score2",0);
        highScore[2] = sharedPreferences.getInt("score3",0);
        highScore[3] = sharedPreferences.getInt("score4",0);
        highScore[4] = sharedPreferences.getInt("score5",0);
        //初始化音效
        gameOnSound = MediaPlayer.create(context,R.raw.nyan);
        killedEnemysound = MediaPlayer.create(context,R.raw.boom2);
        gameOversound = MediaPlayer.create(context,R.raw.gameover1);
        gameOnSound.start();
        this.context = context;
    }

    @Override
    public void run() {
        while(playing){
            //更新
            update();
            //绘制
            draw();
            //控制
            control();
        }
    }
    private void update(){
        player.update();
        boom.setX(-300);
        boom.setY(-300);
        for(Star s:stars){
            s.update(player.getSpeed());
        }
        //!这里是仅仅只放一个敌人的情况，后续若是多个敌人，需要改动！！！！！！！
        //！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
        if(enemies[0].getX() == screenX){
            flag = true;
        }
        for (int i = 0; i < enemyCount; i++){
            enemies[i].update(player.getSpeed());
            //如果玩家与敌人相遇，那么将敌人移动到视野之外
            if (Rect.intersects(player.getDetectCollision(),enemies[i].getDetectCollision())){
                //将爆炸放置在敌人的位置
                boom.setX(enemies[i].getX());
                boom.setY(enemies[i].getY());
                enemies[i].setX(-300);
                score++;
                killedEnemysound.start();
            }else {
                if (player.getDetectCollision().exactCenterX() > enemies[i].getDetectCollision().exactCenterX() ){
                    countMisses++;
                    flag = false;
                    if (countMisses == 3){
                        playing = false;
                        isGameOver = true;
                        gameOnSound.stop();
                        gameOversound.start();
                        for (int j = 3; j > -1; j--){
                            if (score > highScore[j]){
                                highScore[j+1] = highScore[j];
                            }else {
                                highScore[j+1] = score;
                                break;
                            }
                        }
                        SharedPreferences.Editor e = sharedPreferences.edit();
                        for (int k = 0; k < 4; k++){
                            int j = k + 1;
                            e.putInt("score"+j,highScore[k]);
                        }
                        //提交存储的数据
                        e.apply();
                    }
                }
            }
            friend.update(player.getSpeed());
            if (Rect.intersects(player.getDetectCollision(),friend.getDetectCollision())){
                //将爆炸放置在敌人的位置
                boom.setX(friend.getX());
                boom.setY(friend.getY());
                playing = false;
                isGameOver = true;
                gameOnSound.stop();
                gameOversound.start();
                for (int j = 3; j > -1; j--){
                    if (score > highScore[j]){
                        highScore[j+1] = highScore[j];
                    }else {
                        highScore[j+1] = score;
                        break;
                    }
                }
                SharedPreferences.Editor e = sharedPreferences.edit();
                for (int k = 0; k < 4; k++){
                    int j = k + 1;
                    e.putInt("score"+j,highScore[k]);
                }
                //提交存储的数据
                e.apply();
            }
        }
    }
    private void draw(){
        if(surfaceHolder.getSurface().isValid()){
            //锁住canvas，绘制，再解锁
            canvas = surfaceHolder.lockCanvas();
            //背景设置为黑色
            canvas.drawColor(Color.BLACK);
            //星星设置为白色
            paint.setColor(Color.WHITE);
            //绘制星星
            for (Star s:stars){
                paint.setStrokeWidth(s.getStarWidth());
                canvas.drawPoint(s.getX(),s.getY(),paint);
            }
            //绘制分数
            paint.setTextSize(30);
            canvas.drawText("Score:"+score,100,50,paint);
            canvas.drawBitmap(player.getBitmap(),player.getX(),player.getY(),paint);
            //绘制敌人
            for (int i = 0; i < enemyCount; i++){
                canvas.drawBitmap(enemies[i].getBitmap(),enemies[i].getX(),enemies[i].getY(),paint);
            }
            canvas.drawBitmap(boom.getBitmap(),boom.getX(),boom.getY(),paint);
            canvas.drawBitmap(friend.getBitmap(),friend.getX(),friend.getY(),paint);
            //当游戏结束时，显示game over字样
            if (isGameOver){
                paint.setTextSize(150);
                paint.setTextAlign(Paint.Align.CENTER);
                int yPos=(int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2));
                canvas.drawText("Game Over",canvas.getWidth()/2,yPos,paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);
        }
    }
    private void control(){
        try {
            gameThread.sleep(17);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public void pause(){
        playing = false;
        try {
            gameThread.join();
        }catch (InterruptedException e){
        }
    }
    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_UP:
                //当玩家松开屏幕
                player.stopBoosting();
                break;
            case MotionEvent.ACTION_DOWN:
                //当玩家按住屏幕
                player.setBoosting();
                break;
        }
        if (isGameOver){
            if (event.getAction() ==MotionEvent.ACTION_DOWN ){
                context.startActivity(new Intent(context,MainActivity.class));
            }
        }
        return true;
    }
    public static void stopMusic(){
        gameOnSound.stop();
    }

}
