package com.example.dell.colorflood1;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

/**
 * Created by Labadmin on 18/6/2015.
 */
public class TouchImage extends ImageView {
    int locY, width, height, tempColor;
    private List<List<GenerateColor>> generateColor;
    private Paint paint = new Paint();
    private int boxNumX = ColorFlood.boxNumX;
    private int boxNumY = ColorFlood.boxNumY;
    private int space;
    private int screenHeight;

    public TouchImage(Context context, AttributeSet attrs, int width, int height, int boxHeight) {
        super(context, attrs);
        this.screenHeight = height;

        switch (ColorFlood.colorNum) {
            case 6:
                space = (width - 45) / 6;
                break;
            case 10:
                space = (width - 40) / 5;
                break;
        }
    }

    public void getGenerateColor(List<List<GenerateColor>> generateColor){
        this.generateColor = generateColor;
    }

    public void onDraw(Canvas canvas) {
        if (boxNumX >= boxNumY) {
            locY = (int) (generateColor.get(0).get(boxNumX - 1).locX + generateColor.get(boxNumY - 1).get(0).width) + 200;
        }
        else {
            locY = (int) (generateColor.get(boxNumY - 1).get(0).locY + generateColor.get(boxNumY - 1).get(0).width) + 10;
        }
        height = (screenHeight - locY - 115) * 3 / 5;
        for (int i = 0; i < ColorFlood.colorNum; i++) {
            switch (i){
                case -1:
                    paint.setColor(Color.WHITE);
                    break;
                case 5:
                    paint.setARGB(255, 255, 192, 203);
                    break;
                case 0:
                    paint.setColor(Color.RED);
                    break;
                case 6:
                    paint.setARGB(255, 255, 165, 0);
                    break;
                case 1:
                    paint.setColor(Color.YELLOW);
                    break;
                case 7:
                    paint.setARGB(255, 165, 42, 42);
                    break;
                case 2:
                    paint.setColor(Color.GREEN);
                    break;
                case 8:
                    paint.setColor(Color.BLUE);
                    break;
                case 3:
                    paint.setColor(Color.CYAN);
                    break;
                case 9:
                    paint.setColor(Color.MAGENTA);
                    break;
                case 4:
                    paint.setARGB(255, 160, 32, 240);
                    break;
            }
            //paint.setAlpha(ColorFlood.brightnessValue);
            if (i == generateColor.get(0).get(0).color ||
                    i == generateColor.get(boxNumY - 1).get(boxNumX - 1).color && generateColor.get(boxNumY - 1).get(boxNumX - 1).type != 0)
                paint.setAlpha(0);
            switch (ColorFlood.colorNum) {
                case 6:
                    canvas.drawRect(10 + i * (space + 6), screenHeight - height - 10, 10 + space + i * (space + 6), screenHeight - 10, paint);
                    break;
                case 10:
                    if (ColorFlood.type.equals("4 Players")){
                        if (i == generateColor.get(boxNumY - 1).get(0).color || i == generateColor.get(0).get(boxNumX - 1).color)
                             paint.setAlpha(0);
                    }

                    if (i < 5)
                        canvas.drawRect(10 + i * (space + 6), screenHeight - 2 * height - 15, 10 + space + i * (space + 6), screenHeight - height - 15, paint);
                    else
                        canvas.drawRect(10 + (i - 5) * (space + 6), screenHeight - height - 10, 10 + space + (i - 5) * (space + 6), screenHeight - 10, paint);
                    break;
            }
        }
    }

    public boolean onTouchEvent(MotionEvent event, Context context) {
        float eventX = event.getX();
        float eventY = event.getY();
        boolean touched = false;
        int tempTempColor = ColorFlood.colorNum;
        switch (ColorFlood.colorNum) {
            case 6:
                if (eventY > screenHeight - height - 10 && eventY < screenHeight - 10) {
                    for (int i = 0; i < 6; i++) {
                        if (eventX > 10 + i * (space + 6) && eventX < 10 + space + i * (space + 6)) {
                            tempTempColor = i;
                            touched = true;
                        }
                    }
                } else
                    return false;
                break;
            case 10:
                if (eventY > screenHeight - 2 * height - 15 && eventY < screenHeight - height - 15) {
                    for (int i = 0; i < 5; i++) {
                        if (eventX > 10 + i * (space + 6) && eventX < 10 + space + i * (space + 6)) {
                            tempTempColor = i;
                            touched = true;
                        }
                    }
                } else if (eventY > screenHeight - height - 10 && eventY < screenHeight - 10) {
                    for (int i = 5; i < 10; i++) {
                        if (eventX > 10 + (i - 5) * (space + 6) && eventX < 10 + space + (i - 5) * (space + 6)) {
                            tempTempColor = i;
                            touched = true;
                        }
                    }
                } else
                    return false;
                break;
        }

        if (!touched || tempTempColor == generateColor.get(0).get(0).color ||
                tempTempColor == generateColor.get(boxNumY - 1).get(boxNumX - 1).color && generateColor.get(boxNumY - 1).get(boxNumX - 1).type == 2)
            return false;

        if (ColorFlood.type.equals("4 Players")) {
            if (tempTempColor == generateColor.get(boxNumY - 1).get(0).color || tempTempColor == generateColor.get(0).get(boxNumX - 1).color)
                return false;
        }

        tempColor = tempTempColor;
        String error = "";
        /*switch (ColorFlood.type) {
            case "Player vs AI":
                if (tempColor == generateColor.get(0).get(0).color)
                    error = "Cannot choose the same color!";
                else if (tempColor == generateColor.get(boxNumY - 1).get(boxNumX - 1).color)
                    error = "Chosen color same with AI's!";
                break;
            case "2 Players":
                if (tempColor == generateColor.get(0).get(0).color && GamePanel.turn == 1 ||
                        tempColor == generateColor.get(boxNumY - 1).get(boxNumX - 1).color && GamePanel.turn == 2)
                    error = "Cannot choose the same color!";
                else if (tempColor == generateColor.get(0).get(0).color ||
                        tempColor == generateColor.get(boxNumY - 1).get(boxNumX - 1).color)
                    error = "Chosen color same with other player's!";
                break;
            case "4 Players":
                if (tempColor == generateColor.get(0).get(0).color && GamePanel.turn == 1 ||
                        tempColor == generateColor.get(boxNumY - 1).get(boxNumX - 1).color && GamePanel.turn == 2 ||
                        tempColor == generateColor.get(0).get(boxNumX - 1).color && GamePanel.turn == 3 ||
                        tempColor == generateColor.get(boxNumY - 1).get(0).color && GamePanel.turn == 4)
                    error = "Cannot choose the same color!";
                else if (tempColor == generateColor.get(0).get(0).color||
                        tempColor == generateColor.get(boxNumY - 1).get(boxNumX - 1).color||
                        tempColor == generateColor.get(0).get(boxNumX - 1).color||
                        tempColor == generateColor.get(boxNumY - 1).get(0).color)
                    error = "Chosen color same with other player's!";
                break;
        }*/

        if (error.equals("")) {
            for (int j = 0; j < boxNumY; j++) {
                for (int i = 0; i < boxNumX; i++) {
                    if (generateColor.get(j).get(i).type == GamePanel.turn) {
                        generateColor.get(j).get(i).color = tempColor;
                    }
                }
            }
            return true;
        }

        final Toast toast = Toast.makeText(context, error, Toast.LENGTH_SHORT);
        LinearLayout toastLayout = (LinearLayout) toast.getView();
        TextView toastTV = (TextView) toastLayout.getChildAt(0);
        toastTV.setTextSize(15);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);
        return false;
    }
}
