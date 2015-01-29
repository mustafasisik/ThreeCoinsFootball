package com.threecoins.threecoinsfootball;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by mustafa on 29.1.2015.
 */
public class Field extends Activity implements View.OnTouchListener{

    MySurface surfaceViev;
    float x1, x2, x3, y1, y2, y3, sX, sY, fX, fY;
    float dX, dY, aniX, aniY, scaledX, scaledY;
    Bitmap coin1, coin2, coin3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surfaceViev = new MySurface(this);
        surfaceViev.setOnTouchListener(this);
        x1 = 180;
        y1 = 1040;
        x2 = 500;
        y2 = 1040;
        x3 = 340;
        y3 = 800;
        sX = sY = fX = fY = aniX = aniY = scaledX = scaledY = dX = dY = 0;
        coin1 = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        coin2 = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        coin3 = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        setContentView(surfaceViev);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceViev.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceViev.resume();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                sX = event.getX();
                sY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                fX = event.getX();
                fY = event.getY();
                dX = fX - sX;
                dY = fY - sY;
                scaledX = dX;
                scaledY = dY;
                break;
        }
        return true;
    }

    public class MySurface extends SurfaceView implements Runnable{

        SurfaceHolder ourHolder;
        Thread ourThread=null;
        boolean isRunning = true;
        int ballNumber=1;
        public MySurface(Context context) {
            super(context);
            ourHolder = getHolder();
            ourThread = new Thread(this);
            ourThread.start();
        }
        public void pause(){
            isRunning = false;
            while (true){
                try {
                    ourThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            }
            ourThread = null;
        }
        public void resume(){
            isRunning = true;
            ourThread = new Thread(this);
            ourThread.start();
        }

        @Override
        public void run() {
            while (isRunning){
                if (!ourHolder.getSurface().isValid())
                    continue;
                Canvas canvas = ourHolder.lockCanvas();
                canvas.drawRGB(02, 150, 02);
                canvas.drawBitmap(coin1, x1-(coin1.getWidth()/2), y1-(coin1.getHeight()/2), null);
                canvas.drawBitmap(coin2, x2 -(coin2.getWidth()/2)+aniX, y2-(coin2.getHeight()/2)+aniY, null);
                canvas.drawBitmap(coin3, x3 -(coin3.getWidth()/2), y3-(coin3.getHeight()/2), null);
                aniX = aniX + scaledX;
                aniY = aniY + scaledY;
                scaledX = scaledX/3;
                scaledY = scaledY/3;
                ourHolder.unlockCanvasAndPost(canvas);
            }
        }
        public float findDistance(float x1, float y1, float x2, float y2){
            float distance =(float) Math.sqrt((x2-x1)*(x2-x1) + (y2-y1*(y2-y1)));
            return distance;
        }
    }
}
