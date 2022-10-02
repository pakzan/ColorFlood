package com.github.pakzan.colorflood;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class LinearLayoutPanel extends LinearLayout {
    ColorBlock[][] colorPanel;

    public LinearLayoutPanel(Context context) {
        super(context);
        setWillNotDraw(false);
    }

    public LinearLayoutPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public LinearLayoutPanel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;

        int maxBlock = Math.max(colNum, rowNum);
        double boxSize = Math.min(getWidth() / maxBlock, getHeight() / maxBlock);
        double offY = (getHeight() - boxSize * rowNum) / 2;
        double offX = (getWidth() - boxSize * colNum) / 2;

        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                ColorBlock colorBlock = colorPanel[j][i];
                int x = (int) boxSize * i;
                int y = (int) boxSize * j;

                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(colorBlock.getColor().getColorCode(getContext()));
                //paint.setAlpha(ColorFlood.brightnessValue);
                canvas.drawRect((int) (offX + x), (int) (offY + y),
                        (int) (offX + x + boxSize), (int) (offY + y + boxSize), paint);
            }
        }
    }

    public void setColorPanel(ColorBlock[][] colorPanel) {
        this.colorPanel = colorPanel;
    }
}
