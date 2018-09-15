package com.example.dell.colorflood1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Labadmin on 16/6/2015.
 */
public class GenerateColor extends GameObject{

    public int type;
    public int color;
    public int check;
    public int ignore;
    private int boxNumX = ColorFlood.boxNumX;
    private int boxNumY = ColorFlood.boxNumY;
    private static Paint layer = new Paint();
    protected double locX;
    protected double locY;

    public GenerateColor(int i, int j, double width){

        this.width = width;
        height = width;
        x = (int)width * i;
        y = (int)height * j;
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        layer.setStyle(Paint.Style.FILL);
        layer.setColor(Color.WHITE);
    }

    public void setType(int type){this.type = type;}

    public void setColor(int color){

        switch (color){
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
    }

    public void draw(Canvas canvas){

        check = 0;
        ignore = 0;
        locX = x + (ColorFlood.width - width * boxNumX) / 2;
        locY = y + 200;
        if ((ColorFlood.height - width * boxNumY) / 2 > 200)
            locY = y + (ColorFlood.height - width * boxNumY) / 2;

        //paint.setAlpha(ColorFlood.brightnessValue);
        canvas.drawRect((int)locX, (int)locY, (int)(locX + width), (int)(locY + height), paint);

    }
}
