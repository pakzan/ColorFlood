package com.example.dell.colorflood1;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.List;

/**
 * Created by Dell on 24/06/2015.
 */
public class CheckWin {

    private List<List<GenerateColor>> generateColor;
    public static int[] nextTurn = {0, 0, 0, 0};
    int sum, y, textHeight, height, width;
    int [] playerBox;
    int blockNumX = ColorFlood.boxNumX;
    int blockNumY = ColorFlood.boxNumY;
    Paint paint;
    String result, score;

    public CheckWin(List<List<GenerateColor>> generateColor, int height, int width){
        this.generateColor = generateColor;
        this.height = height;
        this.width = width;
        paint = new Paint();
    }

    public boolean checkWin(){
        playerBox = new int[4];
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type > 0)
                    playerBox[generateColor.get(j).get(i).type - 1] += 1;
            }
        }

        sum = 0;
        for (int i: playerBox)
            sum += i;
        if (sum == blockNumX * blockNumY) {
            paint.setTextSize(38);
            y = (textHeight + (int)generateColor.get(0).get(0).locY) / 2;
            switch (ColorFlood.type) {
                case "Player vs AI":
                    if (playerBox[0] > playerBox[1]) {
                        paint.setARGB(255, 173, 216, 230);
                        result = "Congratulation! You win!";
                    } else if (playerBox[0] == playerBox[1]) {
                        paint.setColor(Color.YELLOW);
                        result = "It's a draw!";
                    } else if (playerBox[0] < playerBox[1]) {
                        paint.setColor(Color.RED);
                        result = "You lose!";
                    }
                    break;
                case "2 Players":
                    if (playerBox[0] > playerBox[1]) {
                        paint.setARGB(255, 173, 216, 230);
                        result = "Player 1 win!";
                    } else if (playerBox[0] == playerBox[1]) {
                        paint.setColor(Color.YELLOW);
                        result = "It's a draw!";
                    } else if (playerBox[0] < playerBox[1]) {
                        paint.setARGB(255, 173, 216, 230);
                        result = "Player 2 win!";
                    }
                    break;
                case "4 Players":
                    int winner = playerBox[0];
                    int winnerNum = 0;
                    result = "";
                    paint.setARGB(255, 173, 216, 230);
                    result = "Player 1 win!" + "\n";
                    for (int i = 1; i < 4; i++){
                        if (playerBox[i] > winner) {
                            winner = playerBox[i];
                            result = "Player " + (i + 1) + " win!" + "\n";
                        }else if (playerBox[i] == winner) {
                            paint.setTextSize(25);
                            if ((int)generateColor.get(0).get(0).locY - 3 * (int)(paint.descent() - paint.ascent()) / 2 > 0)
                                y = (textHeight + (int)generateColor.get(0).get(0).locY) / 2 - 3 * (int)(paint.descent() - paint.ascent()) / 2;
                            else
                                y = (int)generateColor.get(0).get(0).locY - 3 * (int)(paint.descent() - paint.ascent()) / 2;
                            winner = playerBox[i];
                            winnerNum += 1;
                            result += "Player " + (i + 1) + " win!" + "\n";
                        }
                    }
                    if (winnerNum == 3) {
                        paint.setTextSize(38);
                        y = (textHeight + (int)generateColor.get(0).get(0).locY) / 2;
                        paint.setColor(Color.YELLOW);
                        result = "It's a draw!";
                    }
                    break;
            }
            return false;
        }
        return true;
    }

    public void DrawResult(Canvas canvas){

        if (sum == blockNumX * blockNumY){
            paint.setTextAlign(Paint.Align.CENTER);
            for (String line: result.split("\n")) {
                canvas.drawText(line, width / 2, y, paint);
                y += paint.descent() - paint.ascent();
            }
        }

        paint.setTextSize(27);
        paint.setColor(Color.WHITE);
        switch (ColorFlood.type) {
            case "Player vs AI":
                paint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText("Player Score = " + Integer.toString(playerBox[0]), 20, 45, paint);
                canvas.drawText("AI Score = " + Integer.toString(playerBox[1]), 20, 85, paint);
                textHeight = 125;
                break;
            case "2 Players":

                paint.setTextAlign(Paint.Align.LEFT);
                score = "";
                for (int i = 0; i < 2; i++)
                    score += "Player " + (i + 1) + " Score = " + Integer.toString(playerBox[i]) + "\n";
                y = 45;
                for (String line: score.split("\n")) {
                    canvas.drawText(line, 20, y, paint);
                    y += 40;
                }
                textHeight = y;

                if (sum != blockNumX * blockNumY){
                    String showTurn = "Player " + GamePanel.turn + "'s turn";
                    paint.setTextAlign(Paint.Align.CENTER);
                    y = (textHeight + (int)generateColor.get(0).get(0).locY) / 2;
                    canvas.drawText(showTurn, width / 2, y, paint);
                }
                break;
            case "4 Players":

                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(23);
                score = "";
                for (int i = 0; i < 4; i++)
                    score += "Player " + (i + 1) + " Score = " + Integer.toString(playerBox[i]) + "\n";
                y = 25;
                int x = 0;
                for (String line: score.split("\n")) {
                    if (nextTurn[x] == 0)
                        paint.setAlpha(255);
                    else
                        paint.setAlpha(150);
                    x++;

                    canvas.drawText(line, 0, y, paint);
                    y += paint.descent() - paint.ascent();
                }
                textHeight = y;

                if (sum != blockNumX * blockNumY){
                    String showTurn = "Player " + GamePanel.turn + "'s turn";
                    paint.setTextAlign(Paint.Align.CENTER);
                    paint.setAlpha(255);
                    y = (textHeight + (int)generateColor.get(0).get(0).locY) / 2;
                    canvas.drawText(showTurn, width / 2, y, paint);
                }
                break;
        }
    }
}
