package com.example.dell.colorflood1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Labadmin on 1ColorFlood.colorNum/ColorFlood.colorNum/2015.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    private static Context context;
    private MainThread thread;
    private static List<List<GenerateColor>> generateColor;
    private static TouchImage touchimage;
    private static SpreadPlayerColor spreadPlayerColor;
    private static SpreadComColor spreadComColor;
    private static CheckWin checkWin;
    public static boolean startPlaying = true;
    public static boolean drawCanvas = false;
    public static boolean allZero;
    private static Random rand = new Random();
    private static double boxHeight;
    public static int blockNumX;
    public static int blockNumY;
    public static int width;
    public static int height;
    public static int turn = 1;
    public static int numOfTurn = 0;
    protected static int[] nextTurn = {1, 2, 3, 4};
    protected static MediaPlayer tapSound;
    public static ExecutorService executor;

    public GamePanel(Context context, int width, int height){
        super(context);
        getHolder().addCallback(this);
        this.width = width;
        this.height = height;
        this.context = context;
        //tapSound = MediaPlayer.create(context, R.raw.tap);

        thread = new MainThread(getHolder(), this);
        executor = Executors.newSingleThreadExecutor();
        executor.execute(thread);

        setFocusable(true);

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        if (!thread.running) {
            reset();
            thread.setRunning(true);
            thread.start();
        }
        if (!checkWin.checkWin())
            reset();
        startPlaying = true;
        ColorFlood.viewChanged = true;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }

    public static void reset(){
        turn = 1;

        switch (ColorFlood.type) {
            case "4 Players":
                nextTurn = new int[]{1, 2, 3, 4};
                int x = rand.nextInt(4);
                turn = nextTurn[x];
                nextTurn[x] = 0;
                CheckWin.nextTurn = new int[]{0, 0, 0, 0};
                break;
        }

        numOfTurn = 0;
        blockNumX = ColorFlood.boxNumX;
        blockNumY = ColorFlood.boxNumY;

        int maxBlock = blockNumY;
        if (blockNumX >= blockNumY)
            maxBlock = blockNumX;
        if ((width - 4) / maxBlock < (height - 4) / maxBlock)
            boxHeight = (width - 4) / maxBlock;
        else
            boxHeight = (height - 4) / maxBlock;

        touchimage = new TouchImage(context, null, width, height, (int)boxHeight);
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
                if (j == 0) {
                    if (i == 1) {
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color);
                    }else if (i > 1) {
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j).get(i - 2).color);
                    }
                }else if (i == 0) {
                    if (j == 1) {
                        //for 4 players
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i + 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(0).get(blockNumX - 2).color);
                    }else if (j == blockNumY - 2 && ColorFlood.type.equals("4 Players")) {
                        //for 4 players
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(0).get(1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(0).get(blockNumX - 2).color ||
                                generateColor.get(j).get(i).color == generateColor.get(1).get(0).color ||
                                generateColor.get(j).get(i).color == generateColor.get(1).get(blockNumX - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i + 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 2).get(i).color);
                    }else if (j > 1) {
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i + 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 2).get(i).color);
                    }
                }else if (i == blockNumX - 1) {
                    if (j == 1) {
                        //for 4 players
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(0).get(1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(1).get(0).color);
                    }else if (j == blockNumY - 2 && ColorFlood.type.equals("4 Players")) {
                        //for 4 players
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(0).get(1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(0).get(blockNumX - 2).color ||
                                generateColor.get(j).get(i).color == generateColor.get(1).get(0).color ||
                                generateColor.get(j).get(i).color == generateColor.get(1).get(blockNumX - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(blockNumY - 2).get(0).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 2).get(i).color);
                    }else if (j > 1) {
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 2).get(i).color);
                    }
                }else if (i > 1 && i < blockNumX - 1 && j > 1) {
                    int choose = rand.nextInt(2);
                    if (choose == 0) {
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 2).get(i).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i + 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i - 1).color);
                    }
                    else {
                        do {
                            generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                        }
                        while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j).get(i - 2).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i + 1).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                                generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i - 1).color);
                    }
                } else if (i == 1 && j > 1) {
                    do {
                        generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                    }
                    while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 2).get(i).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i + 1).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i - 1).color);
                } else if (i > 1 && j == 1) {
                    do {
                        generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                    }
                    while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j).get(i - 2).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i + 1).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i - 1).color);
                }else if (i == 1 && j == 1) {
                    do {
                        generateColor.get(j).get(i).color = rand.nextInt(ColorFlood.colorNum);
                    }
                    while (generateColor.get(j).get(i).color == generateColor.get(j).get(i - 1).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i + 1).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i).color ||
                            generateColor.get(j).get(i).color == generateColor.get(j - 1).get(i - 1).color);
                }
                generateColor.get(j).get(i).setColor(generateColor.get(j).get(i).color);
            }
        }
        //set player's and computer's color
        generateColor.get(0).get(0).color = -1;
        generateColor.get(blockNumY - 1).get(blockNumX - 1).color = -1;
        if (ColorFlood.type.equals("4 Players")){
            generateColor.get(blockNumY - 1).get(0).color = -1;
            generateColor.get(0).get(blockNumX - 1).color = -1;
        }

        //declare type of box
        generateColor.get(0).get(0).setType(1);
        generateColor.get(blockNumY - 1).get(blockNumX - 1).setType(2);
        if (ColorFlood.type.equals("4 Players")){
            generateColor.get(0).get(blockNumX - 1).setType(2);
            generateColor.get(blockNumY - 1).get(0).setType(3);
            generateColor.get(blockNumY - 1).get(blockNumX - 1).setType(4);
        }

        spreadPlayerColor = new SpreadPlayerColor(generateColor);
        spreadComColor = new SpreadComColor(generateColor);
        checkWin = new CheckWin(generateColor, (int)boxHeight, width);
        touchimage.getGenerateColor(generateColor);
    }

    @Override
    public void draw(Canvas canvas){
        if (startPlaying) {
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

            Paint textColor = new Paint();
            textColor.setStyle(Paint.Style.FILL);
            textColor.setTextAlign(Paint.Align.CENTER);
            textColor.setTextSize((float)boxHeight / 2);
            textColor.setColor(Color.BLACK);
            textColor.setAlpha(150);

            switch (ColorFlood.type) {
                case "2 Players":
                    if (numOfTurn % 2 == 0)
                        canvas.drawText("1", (float) (generateColor.get(0).get(0).locX + boxHeight / 2), (float) (generateColor.get(0).get(0).locY + boxHeight / 2), textColor);
                    else
                        canvas.drawText("2", (float) (generateColor.get(blockNumY - 1).get(blockNumX - 1).locX + boxHeight / 2), (float) (generateColor.get(blockNumY - 1).get(blockNumX - 1).locY + boxHeight / 2), textColor);
                    break;
                case "4 Players":
                    switch (turn) {
                        case 1:
                            canvas.drawText("1", (float) (generateColor.get(0).get(0).locX + boxHeight / 2), (float) (generateColor.get(0).get(0).locY + boxHeight / 2), textColor);
                        break;
                        case 2:
                            canvas.drawText("2", (float) (generateColor.get(0).get(blockNumX - 1).locX + boxHeight / 2), (float) (generateColor.get(0).get(blockNumX - 1).locY + boxHeight / 2), textColor);
                        break;
                        case 3:
                            canvas.drawText("3", (float) (generateColor.get(blockNumY - 1).get(0).locX + boxHeight / 2), (float) (generateColor.get(blockNumY - 1).get(0).locY + boxHeight / 2), textColor);
                        break;
                        case 4:
                            canvas.drawText("4", (float) (generateColor.get(blockNumY - 1).get(blockNumX - 1).locX + boxHeight / 2), (float) (generateColor.get(blockNumY - 1).get(blockNumX - 1).locY + boxHeight / 2), textColor);
                            break;
                    }
                break;
            }
            touchimage.draw(canvas);
            checkWin.checkWin();
            checkWin.DrawResult(canvas);
            Bitmap refresh = BitmapFactory.decodeResource(getResources(), R.drawable.refresh);
            Rect src = new Rect(0, 0, refresh.getWidth(), refresh.getHeight());
            Rect des = new Rect(width - 80, 20, width - 20, 80);
            canvas.drawBitmap(refresh, src, des, null);
            drawCanvas = true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_UP) {
            float eventX = event.getX();
            float eventY = event.getY();
            if (eventX > width - 80 && eventX < width - 20 && eventY > 20 && eventY < 80) {
                reset();
                //tapSound.start();
            }else if (checkWin.checkWin() && touchimage.onTouchEvent(event, context)) {
                //tapSound.start();
                if (numOfTurn < 3)
                    numOfTurn++;
                else
                    numOfTurn = 0;

                switch (ColorFlood.type) {
                    case "Player vs AI":
                        spreadPlayerColor.SpreadColor();
                        spreadComColor.SpreadColor();
                        break;
                    case "2 Players":
                        spreadPlayerColor.SpreadColor();
                        if (turn == 1) {
                            turn = 2;
                        }
                        else if (turn == 2) {
                            turn = 1;
                        }
                        break;
                    case "4 Players":
                        spreadPlayerColor.SpreadColor();

                        for (int i = 0; i <= 3; i++) {
                            CheckWin.nextTurn[i] = nextTurn[i];
                        }

                        if (allZero)
                            checkWin.nextTurn = new int[]{0, 0, 0, 0};

                        int x;
                        int prevTurn = turn;
                        do{
                            x = rand.nextInt(4);
                            turn = nextTurn[x];
                        }while(turn == 0 || turn == prevTurn);

                        nextTurn[x] = 0;
                        allZero = true;
                        for (int i = 0; i <= 3; i++){
                            if (nextTurn[i] != 0)
                                allZero = false;
                        }
                        if (allZero) {
                            nextTurn = new int[]{1, 2, 3, 4};
                        }

                        break;
                }
                checkWin.checkWin();
            }
            startPlaying = true;
        }
        return true;
    }


}
