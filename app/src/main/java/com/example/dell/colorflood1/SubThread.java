package com.example.dell.colorflood1;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import java.util.concurrent.Executor;

/**
 * Created by Labadmin on 16/6/2015.
 */
public class SubThread extends Thread {
    public static SurfaceHolder surfaceHolder;
    private Instruction instruction;
    public boolean running;
    public static boolean pause = false;
    public static Canvas canvas;

    public SubThread(SurfaceHolder surfaceHolder, Instruction instruction){
        super();
        this.surfaceHolder = surfaceHolder;
        this.instruction = instruction;

    }

    @Override
    public void run() {

        int FPS = 10;
        long startTime;
        long waitTime;
        long timeMillis;
        long targetTime = 1000 / FPS;

        while (running) {
            if (!pause) {
                startTime = System.nanoTime();
                canvas = null;
                instruction.drawCanvas = false;
                try {
                    canvas = this.surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        this.instruction.draw(canvas);
                    }
                } catch (Exception e) {
                } finally {
                    while (!instruction.drawCanvas) {
                    }
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
            }
        }
    }

    public void setRunning(boolean b){running = b;}
}
