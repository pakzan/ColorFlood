package com.example.dell.colorflood1;

import java.util.List;

/**
 * Created by Dell on 22/06/2015.
 */
public class SpreadPlayerColor {
    List<List<GenerateColor>> generateColor;
    int blockNumX = ColorFlood.boxNumX;
    int blockNumY = ColorFlood.boxNumY;

    public SpreadPlayerColor(List<List<GenerateColor>> generateColor){
        this.generateColor = generateColor;
    }

    public void SpreadColor(){
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type == GamePanel.turn){
                    if (i > 0){
                        if (generateColor.get(j).get(i - 1).type == 0 &&
                                generateColor.get(j).get(i - 1).color == generateColor.get(j).get(i).color){
                            generateColor.get(j).get(i - 1).type = GamePanel.turn;
                        }
                    }
                    if (i < blockNumX - 1){
                        if (generateColor.get(j).get(i + 1).type == 0 &&
                                generateColor.get(j).get(i + 1).color == generateColor.get(j).get(i).color){
                            generateColor.get(j).get(i + 1).type = GamePanel.turn;
                        }
                    }
                    if (j > 0){
                        if (generateColor.get(j - 1).get(i).type == 0 &&
                                generateColor.get(j - 1).get(i).color == generateColor.get(j).get(i).color){
                            generateColor.get(j - 1).get(i).type = GamePanel.turn;
                        }
                    }
                    if (j < blockNumY - 1){
                        if (generateColor.get(j + 1).get(i).type == 0 &&
                                generateColor.get(j + 1).get(i).color == generateColor.get(j).get(i).color){
                            generateColor.get(j + 1).get(i).type = GamePanel.turn;
                        }
                    }
                }
            }
        }

        for (int j = blockNumY - 1; j >= 0; j--) {
            for (int i = blockNumX - 1; i >= 0; i--) {
                if (generateColor.get(j).get(i).type == GamePanel.turn){
                    if (i > 0){
                        if (generateColor.get(j).get(i - 1).type == 0 &&
                                generateColor.get(j).get(i - 1).color == generateColor.get(j).get(i).color){
                            generateColor.get(j).get(i - 1).type = GamePanel.turn;
                        }
                    }
                    if (i < blockNumX - 1){
                        if (generateColor.get(j).get(i + 1).type == 0 &&
                                generateColor.get(j).get(i + 1).color == generateColor.get(j).get(i).color){
                            generateColor.get(j).get(i + 1).type = GamePanel.turn;
                        }
                    }
                    if (j > 0){
                        if (generateColor.get(j - 1).get(i).type == 0 &&
                                generateColor.get(j - 1).get(i).color == generateColor.get(j).get(i).color){
                            generateColor.get(j - 1).get(i).type = GamePanel.turn;
                        }
                    }
                    if (j < blockNumY - 1){
                        if (generateColor.get(j + 1).get(i).type == 0 &&
                                generateColor.get(j + 1).get(i).color == generateColor.get(j).get(i).color){
                            generateColor.get(j + 1).get(i).type = GamePanel.turn;
                        }
                    }
                }
            }
        }
    }
}
