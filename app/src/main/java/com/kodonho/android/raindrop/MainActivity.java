package com.kodonho.android.raindrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    FrameLayout layout;
    Stage stage;
    float displayHeight, displayWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        displayHeight = metrics.heightPixels;
        displayWidth = metrics.widthPixels;

        layout = findViewById(R.id.layout);
        stage = new Stage(this);
        layout.addView(stage);


    }
    public void runStage(View view){
        new Thread(stage).start();
        makeRaindrops();
    }
    public void makeRaindrops(){
        new Thread(){
            public void run(){
                for(int i=0; i<50; i++){
                    try{Thread.sleep(300);}catch(Exception e){}
                    Raindrop raindrop = new Raindrop(displayWidth, displayHeight);
                    stage.addRaindrop(raindrop);
                    raindrop.start();
                }
            }
        }.start();
    }
}
// 빗방울이 움직이는 공간
class Stage extends View implements Runnable {
    List<Raindrop> raindrops;
    public Stage(Context context) {
        super(context);
        raindrops = new ArrayList<>();
    }
    public void addRaindrop(Raindrop raindrop){
        raindrops.add(raindrop);
    }
    @Override
    public void run() {
        while(true){
            postInvalidate();
            try{Thread.sleep(100);}catch(Exception e){e.printStackTrace();}
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        for(Raindrop raindrop : raindrops){
            //System.out.println("raindrop ="+raindrop.x+", "+raindrop.y);
            canvas.drawCircle(raindrop.x, raindrop.y, raindrop.radius, raindrop.paint);
        }
    }
}
//빗방울
class Raindrop extends Thread {
    float x,y;
    float radius;
    float speed;
    Paint paint;
    float displayHeight;
    public Raindrop(float displayWidth, float displayHeight){
        this.displayHeight = displayHeight;
        Random random = new Random();
        radius = random.nextInt(20) + 5;
        y = 0 - radius;
        x = random.nextInt((int) displayWidth);
        speed = random.nextInt(15) + 5;
        paint = new Paint();
        paint.setColor(Color.BLUE);
    }
    public void run(){
        while(y < displayHeight) {
            try{Thread.sleep(100);}catch(Exception e){e.printStackTrace();}
            y = y + speed;
        }
    }
}