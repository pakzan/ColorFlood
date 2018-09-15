package com.example.dell.colorflood1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Dell on 08/07/2015.
 */
public class Instruction extends SurfaceView implements SurfaceHolder.Callback {

    private static Context context;
    private static List<List<GenerateColor>> generateColor;
    private static TouchImage touchimage;
    private static SpreadPlayerColor spreadPlayerColor;
    public static boolean drawCanvas = false;
    private static double boxHeight;
    protected static int blockNumX = 3;
    protected static int blockNumY = 3;
    private static int show = 0;
    public static int numOfTurn = 0;
    public static boolean destroy = false;
    public static ExecutorService executor;
    public static SubThread thread;
    public static Help help;

    public Instruction(Context context){
        super(context);
        getHolder().addCallback(this);
        thread = new SubThread(getHolder(), this);
        help = new Help();

        this.context = context;
        ColorFlood.boxNumX = 3;
        ColorFlood.boxNumY = 3;

        executor = Executors.newSingleThreadExecutor();
        executor.execute(thread);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){
        synchronized (thread.surfaceHolder){
            thread.pause = false;
            thread.surfaceHolder.notifyAll();
        }
        
        if (!thread.running) {
            thread.setRunning(true);
            thread.start();
            reset();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        synchronized (thread.surfaceHolder){
            thread.pause = true;
        }
        if (destroy) {

            ColorFlood.boxNumX = 10;
            ColorFlood.boxNumY = 10;
            boolean retry = true;
            thread.setRunning(false);
            while (retry) {
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
            destroy = false;
        }
    }

    @Override
    public void draw(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);

        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                generateColor.get(j).get(i).setColor(generateColor.get(j).get(i).color);
                generateColor.get(j).get(i).draw(canvas);
            }
        }

        String text = "";
        switch (numOfTurn) {
            case 0 :
                text = "\nThe purpose of ColorFlood is to flood all colors with one color.";
                
                show++;
                TapToContinue(canvas);
                break;
            case 1 :
                text = "\nPlayer starts at the top left corner which represented by the white block.";

                TapToContinue(canvas);
                show++;
                if (show % 10 <= 4) {
                    int locX = (int)(generateColor.get(0).get(0).locX);
                    int locY = (int)(generateColor.get(0).get(0).locY);
                    int width = (int)generateColor.get(0).get(0).width;
                    Paint showBlock = new Paint();
                    showBlock.setARGB(100, 255, 215, 0);
                    showBlock.setStyle(Paint.Style.FILL_AND_STROKE);
                    canvas.drawRect(locX, locY, (locX + width), (locY + width), showBlock);
                }
                break;
            case 2 :
                text = "\nTap on the color list below to change your color.";
                String Tap = "\nTap on the color list below to change your color.";
                Tap(canvas, Tap);

                show++;
                if (show % 10 <= 4) {
                    int locY = (int) (generateColor.get(0).get(2).locX + generateColor.get(2).get(0).width) + 200;
                    int space = (ColorFlood.width - 45) / 6;
                    int height = (ColorFlood.height - locY - 115) * 3 / 5;
                    Paint showBlock = new Paint();
                    showBlock.setStyle(Paint.Style.STROKE);
                    showBlock.setARGB(255, 255, 215, 0);
                    canvas.drawRect(8, ColorFlood.height - height - 12, 11 + space + 5 * (space + 6), ColorFlood.height - 9, showBlock);
                }
                break;
            case 3 :
                text = "\nPlayer cannot choose the colors same with player's and other opponent/s'.";
                Tap = "\nTap on the color list below to change your color.";
                Tap(canvas, Tap);

                show++;
                if (show % 10 <= 4) {
                    int locY = (int) (generateColor.get(0).get(2).locX + generateColor.get(2).get(0).width) + 200;
                    int space = (ColorFlood.width - 45) / 6;
                    int height = (ColorFlood.height - locY - 115) * 3 / 5;
                    Paint showBlock = new Paint();
                    showBlock.setStyle(Paint.Style.STROKE);
                    showBlock.setARGB(255, 255, 215, 0);
                    canvas.drawRect(8, ColorFlood.height - height - 12, 11 + space + 5 * (space + 6), ColorFlood.height - 9, showBlock);
                }
                break;
            default:
                int playerBox = 0;
                for (int j = 0; j < blockNumY; j++) {
                    for (int i = 0; i < blockNumX; i++) {
                        if (generateColor.get(j).get(i).type == 1)
                            playerBox += 1;
                    }
                }
                text = "\nPlayer will earn score for each color flooded. \nTry to flood all the color!\n\n" +
                        "Player Score = " + Integer.toString(playerBox);
                
                if (playerBox == blockNumX * blockNumY) {
                    text = "\nCongratulation! You have flooded all the color!";
                    Tap = "Press the back button to return to main menu.";
                    numOfTurn = -1;
                    Tap(canvas, Tap);
                }
                break;
        }
        TextView description = new TextView(context);
        description.setText(text);
        description.setTextColor(Color.WHITE);
        description.setTextSize(18);
        description.setDrawingCacheEnabled(true);
        description.measure(MeasureSpec.makeMeasureSpec(canvas.getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(canvas.getHeight(), MeasureSpec.EXACTLY));
        description.layout(0, 0, description.getMeasuredWidth(), description.getMeasuredHeight());
        canvas.drawBitmap(description.getDrawingCache(), 0, 0, paint);
        description.setDrawingCacheEnabled(false);

        touchimage.draw(canvas);

        drawCanvas = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_UP) {
            float eventX = event.getX();
            float eventY = event.getY();
            if (numOfTurn == -1) {
                help.onBackPressed();
                surfaceDestroyed(getHolder());
            }
            else if (numOfTurn <= 1)
                numOfTurn++;
            else if (eventX > ColorFlood.width - 80 && eventX < ColorFlood.width - 20 && eventY > 20 && eventY < 80) {
                reset();
            }else if (touchimage.onTouchEvent(event, context)) {
                spreadPlayerColor.SpreadColor();
                numOfTurn++;
            }
        }
        return true;
    }

    public static void reset(){
        numOfTurn = 0;

        int maxBlock = blockNumY;
        if (blockNumX >= blockNumY)
            maxBlock = blockNumX;
        if ((ColorFlood.width - 4) / maxBlock < (ColorFlood.height - 4) / maxBlock)
            boxHeight = (ColorFlood.width - 4) / maxBlock;
        else
            boxHeight = (ColorFlood.height - 4) / maxBlock;

        touchimage = new TouchImage(context, null, ColorFlood.width, ColorFlood.height, (int)boxHeight);
        generateColor = new ArrayList<List<GenerateColor>>();
        for(int j = 0; j < blockNumY; j++){
            List<GenerateColor> list = new ArrayList<GenerateColor>();
            generateColor.add(list);
            for(int i = 0; i < blockNumX; i++){
                list.add(new GenerateColor(i, j, boxHeight));
            }
        }

        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                generateColor.get(j).get(i).setType(0);
                generateColor.get(j).get(i).color = (i + j * 3 - 1) % 6;
                generateColor.get(j).get(i).setColor(generateColor.get(j).get(i).color);
            }
        }

        //declare type of box
        generateColor.get(0).get(0).setType(1);

        spreadPlayerColor = new SpreadPlayerColor(generateColor);
        touchimage.getGenerateColor(generateColor);
    }

    public static void TapToContinue(Canvas canvas){
        Paint tap = new Paint();
        String text = "";
        if (show % 23 <= 4){
            text = "Tap to continue";
        }else if (show % 23 <= 9){
            text = "Tap to continue.";
        }else if(show % 23 <= 14){
            text = "Tap to continue..";
        }else if(show % 23 <= 19) {
            text = "Tap to continue...";
        }

        tap.setTextSize(28);
        tap.setColor(Color.WHITE);
        tap.setAlpha(200);
        tap.setStyle(Paint.Style.FILL);
        tap.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text , (int)generateColor.get(blockNumY - 1).get(1).locX - 5,
                (int)(generateColor.get(blockNumY - 1).get(1).locY + generateColor.get(0).get(0).width + 25), tap);

        tap.setARGB(150, 173, 216, 230);
        canvas.drawText(text , (int)generateColor.get(blockNumY - 1).get(1).locX - 5,
                (int)(generateColor.get(blockNumY - 1).get(1).locY + generateColor.get(0).get(0).width + 25), tap);
    }

    public static void Tap(Canvas canvas, String Tap){
        Paint tap = new Paint();
        String text = Tap;

        TextView description = new TextView(context);
        description.setText(text);
        description.setTextColor(Color.WHITE);
        description.setTextSize(18);
        description.setDrawingCacheEnabled(true);
        description.measure(MeasureSpec.makeMeasureSpec(canvas.getWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(canvas.getHeight(), MeasureSpec.EXACTLY));
        description.layout(0, 0, description.getMeasuredWidth(), description.getMeasuredHeight());
        canvas.drawBitmap(description.getDrawingCache(), 0,
                (int)(generateColor.get(blockNumY - 1).get(1).locY + generateColor.get(0).get(0).width + 15), tap);

        description.setTextColor(Color.argb(150, 173, 216, 230));
        canvas.drawBitmap(description.getDrawingCache(), 0,
                (int)(generateColor.get(blockNumY - 1).get(1).locY + generateColor.get(0).get(0).width + 15), tap);
        description.setDrawingCacheEnabled(false);

    }

}
