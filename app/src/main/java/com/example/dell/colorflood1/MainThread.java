package com.example.dell.colorflood1;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Labadmin on 16/6/2015.
 */
public class MainThread extends Thread {
    private  SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    public boolean running;
    public static Canvas canvas;
    Executor executor;
    private static boolean block = false;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;

    }

    @Override
    public void run() {


        int FPS = 5;
        long startTime;
        long waitTime;
        long timeMillis;
        long targetTime = 1000 / FPS;

        while (running) {

            if (gamePanel.startPlaying) {
                startTime = System.nanoTime();
                canvas = null;
                gamePanel.drawCanvas = false;
                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        this.gamePanel.draw(canvas);
                    }
                } catch (Exception e) {
                } finally {
                    while (!gamePanel.drawCanvas){}
                        try {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        } catch (Exception e) {
                    }
                }

                timeMillis = (System.nanoTime() - startTime) / 1000000;
                waitTime = targetTime - timeMillis;
                try {
                    this.sleep(waitTime);
                } catch (Exception e) {
                }
                gamePanel.startPlaying = false;

            }
        }
    }


    public void setRunning(boolean b){running = b;}
}
