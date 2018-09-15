package com.example.dell.colorflood1;

        import android.widget.Toast;

        import java.util.List;
        import java.util.Random;

/**
 * Created by Dell on 22/06/2015.
 */
public class SpreadComColor {

    List<List<GenerateColor>> generateColor;
    private int numOfTurn = 0;
    private int cn1;
    private int blockNumX = ColorFlood.boxNumX;
    private int blockNumY = ColorFlood.boxNumY;
    private static int sum = 0;
    private static boolean block;
    private static boolean[] Block;

    public SpreadComColor(List<List<GenerateColor>> generateColor) {
        this.generateColor = generateColor;
    }

    public void SpreadColor(){
        if (ColorFlood.mode.equals("Easy"))
            ChooseColor1();
        else
            ChooseColor2();

        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type == 2) {
                    generateColor.get(j).get(i).color = cn1;
                }
            }
        }

        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type == 2){
                    if (i > 0){
                        if (generateColor.get(j).get(i - 1).type == 0 &&
                                generateColor.get(j).get(i - 1).color == generateColor.get(j).get(i).color){
                            generateColor.get(j).get(i - 1).type = 2;
                        }
                    }
                    if (i < blockNumX - 1){
                        if (generateColor.get(j).get(i + 1).type == 0 &&
                                generateColor.get(j).get(i + 1).color == generateColor.get(j).get(i).color){
                            generateColor.get(j).get(i + 1).type = 2;
                        }
                    }
                    if (j > 0){
                        if (generateColor.get(j - 1).get(i).type == 0 &&
                                generateColor.get(j - 1).get(i).color == generateColor.get(j).get(i).color){
                            generateColor.get(j - 1).get(i).type = 2;
                        }
                    }
                    if (j < blockNumY - 1){
                        if (generateColor.get(j + 1).get(i).type == 0 &&
                                generateColor.get(j + 1).get(i).color == generateColor.get(j).get(i).color){
                            generateColor.get(j + 1).get(i).type = 2;
                        }
                    }
                }
            }
        }

        for (int j = blockNumY - 1; j >= 0; j--) {
            for (int i = blockNumX - 1; i >= 0; i--) {
                if (generateColor.get(j).get(i).type == 2){
                    if (i > 0){
                        if (generateColor.get(j).get(i - 1).type == 0 &&
                                generateColor.get(j).get(i - 1).color == generateColor.get(j).get(i).color){
                            generateColor.get(j).get(i - 1).type = 2;
                        }
                    }
                    if (i < blockNumX - 1){
                        if (generateColor.get(j).get(i + 1).type == 0 &&
                                generateColor.get(j).get(i + 1).color == generateColor.get(j).get(i).color){
                            generateColor.get(j).get(i + 1).type = 2;
                        }
                    }
                    if (j > 0){
                        if (generateColor.get(j - 1).get(i).type == 0 &&
                                generateColor.get(j - 1).get(i).color == generateColor.get(j).get(i).color){
                            generateColor.get(j - 1).get(i).type = 2;
                        }
                    }
                    if (j < blockNumY - 1){
                        if (generateColor.get(j + 1).get(i).type == 0 &&
                                generateColor.get(j + 1).get(i).color == generateColor.get(j).get(i).color){
                            generateColor.get(j + 1).get(i).type = 2;
                        }
                    }
                }
            }
        }
    }

    public void ChooseColor1() {
        //find color with most number of boxes
        int[] comColor = new int[ColorFlood.colorNum];
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type == 2) {
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0)
                            comColor[generateColor.get(j).get(i - 1).color] += 1;
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0)
                            comColor[generateColor.get(j).get(i + 1).color] += 1;
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0)
                            comColor[generateColor.get(j - 1).get(i).color] += 1;
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0)
                            comColor[generateColor.get(j + 1).get(i).color] += 1;
                    }
                }
            }
        }

        //cancel out color choice that is same with player color
        comColor[generateColor.get(0).get(0).color] = 0;

        //get color with most number of boxes
        cn1 = 0;
        for (int n = 0; n < ColorFlood.colorNum; n++) {
            if (comColor[cn1] < comColor[n]){
                cn1 = n;
            }
        }
        if (comColor[cn1] == 0) {
            Random rand = new Random();
            do {
                cn1 = rand.nextInt(ColorFlood.colorNum);
            }
            while (cn1 == generateColor.get(0).get(0).color || cn1 == generateColor.get(blockNumY - 1).get(blockNumX - 1).color);
        }
    }

    public void ChooseColor2() {
        //get computer edge block
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                generateColor.get(j).get(i).ignore = 0;
                generateColor.get(j).get(i).check = 0;
                if (generateColor.get(j).get(i).type == 0) {
                    if (i > 0 && i < blockNumX - 1) {
                        if (generateColor.get(j).get(i - 1).type == 0 && generateColor.get(j).get(i + 1).type == 2)
                            generateColor.get(j).get(i).check = 2;
                    }
                    if (j > 0 && j < blockNumY - 1) {
                        if (generateColor.get(j - 1).get(i).type == 0 && generateColor.get(j + 1).get(i).type == 2)
                            generateColor.get(j).get(i).check = 2;
                    }
                }
            }
        }

        //cancel out neutral box inside computer area
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).check == 2) {
                    if (i > 2 && i < blockNumX - 1) {
                        if (generateColor.get(j).get(i - 2).type == 2 && generateColor.get(j).get(i + 1).type == 2)
                            generateColor.get(j).get(i).check = -2;
                        if (generateColor.get(j).get(i - 3).type == 2 && generateColor.get(j).get(i + 1).type == 2)
                            generateColor.get(j).get(i).check = -2;
                    }
                    if (j > 2 && j < blockNumY - 1) {
                        if (generateColor.get(j - 2).get(i).type == 2 && generateColor.get(j + 1).get(i).type == 2)
                            generateColor.get(j).get(i).check = -2;
                        if (generateColor.get(j - 3).get(i).type == 2 && generateColor.get(j + 1).get(i).type == 2)
                            generateColor.get(j).get(i).check = -2;
                    }
                }
            }
        }

        //cancel out neutral edge box that is not potentially needed
        for (int j = 1; j < blockNumY - 1; j++) {
            for (int i = 1; i < blockNumX - 1; i++) {
                if (generateColor.get(j).get(i).check == 2) {
                    if (generateColor.get(j - 1).get(i - 1).type == 2 && generateColor.get(j + 1).get(i + 1).type == 2 ||
                            generateColor.get(j + 1).get(i - 1).type == 2 && generateColor.get(j - 1).get(i + 1).type == 2)
                        generateColor.get(j).get(i).check = -2;
                }
            }
        }

        //cancel out leftmost or rightmost neutral edge box that is not potentially needed
        for (int j = 1; j < blockNumY - 1; j++) {
            if (generateColor.get(j).get(0).check == 2 &&
                    generateColor.get(j + 1).get(1).type == 2 && generateColor.get(j - 1).get(1).type == 2) {
                generateColor.get(j).get(0).check = -2;
            }
            if (generateColor.get(j).get(blockNumX - 1).check == 2 &&
                    generateColor.get(j + 1).get(blockNumX- 2).type == 2 && generateColor.get(j - 1).get(blockNumX- 2).type == 2) {
                generateColor.get(j).get(blockNumX - 1).check = -2;
            }
        }

        //cancel out topmost or bottommost neutral edge box that is not potentially needed
        for (int i = 1; i < blockNumX - 1; i++) {
            if (generateColor.get(0).get(i).check == 2 &&
                    generateColor.get(1).get(i + 1).type == 2 && generateColor.get(1).get(i - 1).type == 2) {
                generateColor.get(0).get(i).check = -2;
            }
            if (generateColor.get(blockNumY - 1).get(i).check == 2 &&
                    generateColor.get(blockNumY - 2).get(i + 1).type == 2 && generateColor.get(blockNumY - 2).get(i - 1).type == 2) {
                generateColor.get(blockNumY - 1).get(i).check = -2;
            }
        }

        //get box that is in between computer and player box
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type == 0) {
                    if (i > 0 && i < blockNumX - 1) {
                        if (generateColor.get(j).get(i - 1).type == 1 && generateColor.get(j).get(i + 1).type == 2)
                            generateColor.get(j).get(i).check = 2;
                    }
                    if (j > 0 && j < blockNumY - 1) {
                        if (generateColor.get(j - 1).get(i).type == 1 && generateColor.get(j + 1).get(i).type == 2)
                            generateColor.get(j).get(i).check = 2;
                    }
                }
            }
        }

        //get other box that is same color with neutral edge box
        //top left to bottom right
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).check == 2) {
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0 && generateColor.get(j).get(i - 1).check == 0 &&
                                generateColor.get(j).get(i - 1).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j).get(i - 1).check = 2;
                        }
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0 && generateColor.get(j).get(i + 1).check == 0 &&
                                generateColor.get(j).get(i + 1).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j).get(i + 1).check = 2;
                        }
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0 && generateColor.get(j - 1).get(i).check == 0 &&
                                generateColor.get(j - 1).get(i).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j - 1).get(i).check = 2;
                        }
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0 && generateColor.get(j + 1).get(i).check == 0 &&
                                generateColor.get(j + 1).get(i).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j + 1).get(i).check = 2;
                        }
                    }
                }
            }
        }

        //get other box that is same color with neutral edge box
        //bottom right to top left
        for (int j = blockNumY - 1; j >= 0; j--) {
            for (int i = blockNumX - 1; i >= 0; i--) {
                if (generateColor.get(j).get(i).check == 2) {
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0 && generateColor.get(j).get(i - 1).check == 0 &&
                                generateColor.get(j).get(i - 1).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j).get(i - 1).check = 2;
                        }
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0 && generateColor.get(j).get(i + 1).check == 0 &&
                                generateColor.get(j).get(i + 1).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j).get(i + 1).check = 2;
                        }
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0 && generateColor.get(j - 1).get(i).check == 0 &&
                                generateColor.get(j - 1).get(i).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j - 1).get(i).check = 2;
                        }
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0 && generateColor.get(j + 1).get(i).check == 0 &&
                                generateColor.get(j + 1).get(i).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j + 1).get(i).check = 2;
                        }
                    }
                }
            }
        }

        //cancel out current computer box surface area
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type == 2) {
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0)
                            generateColor.get(j).get(i - 1).ignore = 2;
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0)
                            generateColor.get(j).get(i + 1).ignore = 2;
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0)
                            generateColor.get(j - 1).get(i).ignore = 2;
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0)
                            generateColor.get(j + 1).get(i).ignore = 2;
                    }
                }
            }
        }

        //count potentially computer surface area
        int[] comColor = new int[ColorFlood.colorNum];
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).check == 2) {
                    int SArea = 0;
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0 && generateColor.get(j).get(i - 1).ignore != 2)
                            SArea += 1;
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0 && generateColor.get(j).get(i + 1).ignore != 2)
                            SArea += 1;
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0 && generateColor.get(j - 1).get(i).ignore != 2)
                            SArea += 1;
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0 && generateColor.get(j + 1).get(i).ignore != 2)
                            SArea += 1;
                    }

                    comColor[generateColor.get(j).get(i).color] += SArea;
                }
            }
        }

        //cancel out color choice that is same with player color
        comColor[generateColor.get(0).get(0).color] = 0;

        //get best color choice
        cn1 = 0;
        int cn2 = 0;
        boolean compareColor = false;
        for (int n = 0; n < ColorFlood.colorNum; n++) {
            if (comColor[cn1] < comColor[n])
                cn1 = n;
            if (comColor[cn1] == comColor[n] && cn1 != n) {
                cn2 = n;
                compareColor = true;
            }
        }

        //if has two best color choice, then choose the color that is nearer to the middle
        if (compareColor) {
            int[] locX = {blockNumX, blockNumX};
            int[] locY = {blockNumY, blockNumY};
            for (int j = 1; j < blockNumY / 2; j++) {
                for (int i = blockNumX - 1; i >= blockNumX / 2; i--) {
                    if (generateColor.get(j).get(i).type == 1) {
                        if (generateColor.get(j - 1).get(i).color == cn1 && generateColor.get(j).get(i - 1).color == cn2 ||
                                generateColor.get(j - 1).get(i).color == cn2 && generateColor.get(j).get(i - 1).color == cn1) {
                            locX[0] = i - 1;
                            locY[0] = j;
                        }
                    }
                }
            }
            for (int j = blockNumY - 1; j >= blockNumY / 2; j--) {
                for (int i = 1; i < blockNumX / 2; i++) {
                    if (generateColor.get(j).get(i).type == 1) {
                        if (generateColor.get(j).get(i - 1).color == cn1 && generateColor.get(j - 1).get(i).color == cn2 ||
                                generateColor.get(j).get(i - 1).color == cn2 && generateColor.get(j - 1).get(i).color == cn1) {
                            locX[1] = i;
                            locY[1] = j - 1;
                        }
                    }
                }
            }
            if (locX[0] != blockNumX && locX[1] != blockNumX) {
                if (locX[0] < locY[1])
                    cn1 = generateColor.get(locY[0]).get(locX[0]).color;
                else
                    cn1 = generateColor.get(locY[1]).get(locX[1]).color;
            }
        }

        //if no neutral edge color, then choose neutral box that is inside computer area
        if (comColor[cn1] == 0) {
            boolean noComColor = true;
            for (int j = 0; j < blockNumY; j++) {
                for (int i = 0; i < blockNumX; i++) {
                    if (generateColor.get(j).get(i).type == 2) {
                        if (i > 0) {
                            if (generateColor.get(j).get(i - 1).type == 0 &&
                                    generateColor.get(j).get(i - 1).color != generateColor.get(0).get(0).color) {
                                cn1 = generateColor.get(j).get(i - 1).color;
                                noComColor = false;
                                break;
                            }
                        }
                        if (i < blockNumX - 1) {
                            if (generateColor.get(j).get(i + 1).type == 0 &&
                                    generateColor.get(j).get(i + 1).color != generateColor.get(0).get(0).color) {
                                cn1 = generateColor.get(j).get(i + 1).color;
                                noComColor = false;
                                break;
                            }
                        }
                        if (j > 0) {
                            if (generateColor.get(j - 1).get(i).type == 0 &&
                                    generateColor.get(j - 1).get(i).color != generateColor.get(0).get(0).color) {
                                cn1 = generateColor.get(j - 1).get(i).color;
                                noComColor = false;
                                break;
                            }
                        }
                        if (j < blockNumY - 1) {
                            if (generateColor.get(j + 1).get(i).type == 0 &&
                                    generateColor.get(j + 1).get(i).color != generateColor.get(0).get(0).color) {
                                cn1 = generateColor.get(j + 1).get(i).color;
                                noComColor = false;
                                break;
                            }
                        }
                    }
                }
            }

            //choose neutral box which is in between player and computer box
            //leftmost or rightmost
            for (int j = 1; j < blockNumY - 1; j++) {
                if (generateColor.get(j).get(0).type == 0) {
                    if (generateColor.get(j).get(1).type == 1 || generateColor.get(j - 1).get(0).type == 1) {
                        if (generateColor.get(j).get(1).type == 2 || generateColor.get(j + 1).get(0).type == 2) {
                            cn1 = generateColor.get(j).get(0).color;
                            noComColor = false;
                            break;
                        }
                    }
                }
                if (generateColor.get(j).get(blockNumX - 1).type == 0) {
                    if (generateColor.get(j).get(blockNumX - 2).type == 1 || generateColor.get(j - 1).get(blockNumX - 1).type == 1) {
                        if (generateColor.get(j).get(blockNumX - 2).type == 2 || generateColor.get(j + 1).get(blockNumX - 1).type == 2) {
                            cn1 = generateColor.get(j).get(blockNumX - 1).color;
                            noComColor = false;
                            break;
                        }
                    }
                }
                //middle neutral box
                for (int i = 1; i < blockNumX - 1; i++) {
                    if (generateColor.get(j).get(i).type == 0) {
                        if (generateColor.get(j - 1).get(i).type == 1 || generateColor.get(j).get(i - 1).type == 1) {
                            if (generateColor.get(j + 1).get(i).type == 2 || generateColor.get(j).get(i + 1).type == 2) {
                                cn1 = generateColor.get(j).get(i).color;
                                noComColor = false;
                                break;
                            }
                        }
                    }
                }
            }
            //topmost or bottommost
            for (int i = 1; i < blockNumX - 1; i++) {
                if (generateColor.get(0).get(i).type == 0) {
                    if (generateColor.get(1).get(i).type == 1 || generateColor.get(0).get(i - 1).type == 1) {
                        if (generateColor.get(1).get(i).type == 2 || generateColor.get(0).get(i + 1).type == 2) {
                            cn1 = generateColor.get(0).get(i).color;
                            noComColor = false;
                            break;
                        }
                    }
                }
                if (generateColor.get(blockNumY - 1).get(i).type == 0) {
                    if (generateColor.get(blockNumY - 1).get(i - 1).type == 1 || generateColor.get(blockNumY - 2).get(i).type == 1) {
                        if (generateColor.get(blockNumY - 1).get(i + 1).type == 2 || generateColor.get(blockNumY - 2).get(i).type == 2) {
                            cn1 = generateColor.get(blockNumY - 1).get(i).color;
                            noComColor = false;
                            break;
                        }
                    }
                }
            }

            //if no neutral box that is inside computer area, then choose random color
            if (noComColor) {
                Random rand = new Random();
                do {
                    cn1 = rand.nextInt(ColorFlood.colorNum);
                }
                while (cn1 == generateColor.get(0).get(0).color || cn1 == generateColor.get(blockNumY - 1).get(blockNumX - 1).color);
                return;
            }
        }
        if (ColorFlood.mode.equals("Hard"))
            Strategy1(comColor);
    }

    public void Strategy1(int[] comColor){
        //get player edge block
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type == 0 && generateColor.get(j).get(i).check == 0) {
                    if (i > 0 && i < blockNumX - 1) {
                        if (generateColor.get(j).get(i - 1).type == 1 && generateColor.get(j).get(i + 1).type == 0)
                            generateColor.get(j).get(i).check = 1;
                    }
                    if (j > 0 && j < blockNumY - 1) {
                        if (generateColor.get(j - 1).get(i).type == 1 && generateColor.get(j + 1).get(i).type == 0)
                            generateColor.get(j).get(i).check = 1;
                    }
                }
            }
        }

        //cancel out neutral box inside player area
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).check == 1) {
                    if (i > 0 && i < blockNumX - 3) {
                        if (generateColor.get(j).get(i - 1).type == 1 && generateColor.get(j).get(i + 2).type == 1)
                            generateColor.get(j).get(i).check = -1;
                        if (generateColor.get(j).get(i - 1).type == 1 && generateColor.get(j).get(i + 3).type == 1)
                            generateColor.get(j).get(i).check = -1;
                    }
                    if (j > 0 && j < blockNumY - 3) {
                        if (generateColor.get(j - 1).get(i).type == 1 && generateColor.get(j + 2).get(i).type == 1)
                            generateColor.get(j).get(i).check = -1;
                        if (generateColor.get(j - 1).get(i).type == 1 && generateColor.get(j + 3).get(i).type == 1)
                            generateColor.get(j).get(i).check = -1;
                    }
                }
            }
        }

        //cancel out neutral edge box that is not potentially needed
        for (int j = 1; j < blockNumY - 1; j++) {
            for (int i = 1; i < blockNumX - 1; i++) {
                if (generateColor.get(j).get(i).check == 1) {
                    if (generateColor.get(j - 1).get(i - 1).type == 1 && generateColor.get(j + 1).get(i + 1).type == 1 ||
                            generateColor.get(j + 1).get(i - 1).type == 1 && generateColor.get(j - 1).get(i + 1).type == 1)
                        generateColor.get(j).get(i).check = -1;
                }
            }
        }

        //cancel out leftmost or rightmost neutral edge box that is not potentially needed
        for (int j = 1; j < blockNumY - 1; j++) {
            if (generateColor.get(j).get(0).check == 1 &&
                    generateColor.get(j + 1).get(1).type == 1 && generateColor.get(j - 1).get(1).type == 1) {
                generateColor.get(j).get(0).check = -1;
            }
            if (generateColor.get(j).get(blockNumX - 1).check == 1 &&
                    generateColor.get(j + 1).get(blockNumX- 2).type == 1 && generateColor.get(j - 1).get(blockNumX- 2).type == 1) {
                generateColor.get(j).get(blockNumX - 1).check = -1;
            }
        }

        //cancel out topmost or bottommost neutral edge box that is not potentially needed
        for (int i = 1; i < blockNumX - 1; i++) {
            if (generateColor.get(0).get(i).check == 1 &&
                    generateColor.get(1).get(i + 1).type == 1 && generateColor.get(1).get(i - 1).type == 1) {
                generateColor.get(0).get(i).check = -1;
            }
            if (generateColor.get(blockNumY - 1).get(i).check == 1 &&
                    generateColor.get(blockNumY - 2).get(i + 1).type == 1 && generateColor.get(blockNumY - 2).get(i - 1).type == 1) {
                generateColor.get(blockNumY - 1).get(i).check = -1;
            }
        }

        //get other box that is same color with neutral edge box
        //top left to bottom right
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).check == 1) {
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0 && generateColor.get(j).get(i - 1).check == 0 &&
                                generateColor.get(j).get(i - 1).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j).get(i - 1).check = 1;
                        }
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0 && generateColor.get(j).get(i + 1).check == 0 &&
                                generateColor.get(j).get(i + 1).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j).get(i + 1).check = 1;
                        }
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0 && generateColor.get(j - 1).get(i).check == 0 &&
                                generateColor.get(j - 1).get(i).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j - 1).get(i).check = 1;
                        }
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0 && generateColor.get(j + 1).get(i).check == 0 &&
                                generateColor.get(j + 1).get(i).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j + 1).get(i).check = 1;
                        }
                    }
                }
            }
        }

        //get other box that is same color with neutral edge box
        //bottom right to top left
        for (int j = blockNumY - 1; j >= 0; j--) {
            for (int i = blockNumX - 1; i >= 0; i--) {
                if (generateColor.get(j).get(i).check == 1) {
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0 && generateColor.get(j).get(i - 1).check == 0 &&
                                generateColor.get(j).get(i - 1).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j).get(i - 1).check = 1;
                        }
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0 && generateColor.get(j).get(i + 1).check == 0 &&
                                generateColor.get(j).get(i + 1).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j).get(i + 1).check = 1;
                        }
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0 && generateColor.get(j - 1).get(i).check == 0 &&
                                generateColor.get(j - 1).get(i).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j - 1).get(i).check = 1;
                        }
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0 && generateColor.get(j + 1).get(i).check == 0 &&
                                generateColor.get(j + 1).get(i).color == generateColor.get(j).get(i).color) {
                            generateColor.get(j + 1).get(i).check = 1;
                        }
                    }
                }
            }
        }

        //cancel out current player box surface area
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type == 1) {
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0)
                            generateColor.get(j).get(i - 1).ignore = 1;
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0)
                            generateColor.get(j).get(i + 1).ignore = 1;
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0)
                            generateColor.get(j - 1).get(i).ignore = 1;
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0)
                            generateColor.get(j + 1).get(i).ignore = 1;
                    }
                }
            }
        }

        //count potentially player surface area
        int[] PlayerColor = new int[ColorFlood.colorNum];
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).check == 1) {
                    int SArea = 0;
                    if (i > 0) {
                        if (generateColor.get(j).get(i - 1).type == 0 && generateColor.get(j).get(i - 1).ignore != 1)
                            SArea += 1;
                    }
                    if (i < blockNumX - 1) {
                        if (generateColor.get(j).get(i + 1).type == 0 && generateColor.get(j).get(i + 1).ignore != 1)
                            SArea += 1;
                    }
                    if (j > 0) {
                        if (generateColor.get(j - 1).get(i).type == 0 && generateColor.get(j - 1).get(i).ignore != 1)
                            SArea += 1;
                    }
                    if (j < blockNumY - 1) {
                        if (generateColor.get(j + 1).get(i).type == 0 && generateColor.get(j + 1).get(i).ignore != 1)
                            SArea += 1;
                    }

                    PlayerColor[generateColor.get(j).get(i).color] += SArea;
                }
            }
        }

        //get player best color choice
        int pn1 = 0;
        for (int n = 0; n < ColorFlood.colorNum; n++) {
            if (PlayerColor[pn1] < PlayerColor[n])
                pn1 = n;
        }

        //get player second best color choice
        int pn2 = 0;
        for (int n = 0; n < ColorFlood.colorNum; n++) {
            if (PlayerColor[pn2] < PlayerColor[n] && pn2 != pn1)
                pn2 = n;
        }

        numOfTurn += 1;

        //if player can choose color with potentially more boxes, then choose player color
        if (PlayerColor[pn1] - comColor[cn1] > PlayerColor[pn2] - comColor[pn1] && numOfTurn >= 5){
            if (pn1 != generateColor.get(0).get(0).color && pn1 != generateColor.get(blockNumY - 1).get(blockNumX - 1).color)
                cn1 = pn1;
        }

        //in the second turn, computer will choose the box near the center
        if (numOfTurn == 2 && generateColor.get(blockNumY - 2).get(blockNumX - 2).type == 0) {
            if (generateColor.get(blockNumY - 2).get(blockNumX - 2).color != generateColor.get(0).get(0).color &&
                    generateColor.get(blockNumY - 2).get(blockNumX - 2).color != generateColor.get(blockNumY - 1).get(blockNumX - 1).color) {
                cn1 = generateColor.get(blockNumY - 2).get(blockNumX - 2).color;
                return;
            }
        }

        //in the third turn, computer will choose the box near the center
        if (numOfTurn == 3 && generateColor.get(blockNumY - 2).get(blockNumX - 2).type == 0) {
            if (generateColor.get(blockNumY - 1).get(blockNumX - 3).type == 2 &&
                    generateColor.get(blockNumY - 2).get(blockNumX - 3).color != generateColor.get(0).get(0).color &&
                    generateColor.get(blockNumY - 2).get(blockNumX - 3).color != generateColor.get(blockNumY - 1).get(blockNumX - 1).color) {
                cn1 = generateColor.get(blockNumY - 2).get(blockNumX - 3).color;
                return;
            }
            else if (generateColor.get(blockNumY - 3).get(blockNumX - 2).type == 2 &&
                    generateColor.get(blockNumY - 3).get(blockNumX - 2).color != generateColor.get(0).get(0).color &&
                    generateColor.get(blockNumY - 3).get(blockNumX - 2).color != generateColor.get(blockNumY - 1).get(blockNumX - 1).color) {
                cn1 = generateColor.get(blockNumY - 3).get(blockNumX - 2).color;
                return;
            }
        }


        //determine whether to block the the way or not
        sum = 0;
        for (int j = 0; j < blockNumY; j++) {
            for (int i = 0; i < blockNumX; i++) {
                if (generateColor.get(j).get(i).type > 0)
                    sum += 1;
            }
        }
        block = false;
        Block = new boolean[]{false, false};

        for (int j = blockNumY - 2; j >= 0 && generateColor.get(j).get(blockNumX - 1).type != 1; j--) {
            block:
            for (int i = blockNumX - 2; i >= (blockNumX - 1) * 2 / 3; i--) {
                if (generateColor.get(j).get(i).type == 1){
                    for (int z = i + 1; z <= blockNumX - 1; z++){
                        if (generateColor.get(j).get(z).type == 2){
                            block = false;
                            break block;
                        }else {
                            block = true;
                            Block[0] = true;
                        }
                    }
                }
            }
        }
        for (int i = blockNumX - 2; i >= 0 && generateColor.get(blockNumY - 1).get(i).type != 1; i--) {
            block:
            for (int j = blockNumY - 2; j >= (blockNumY - 1) * 2 / 3; j--) {
                if (generateColor.get(j).get(i).type == 1){
                    for (int z = j + 1; z <= blockNumY - 1; z++){
                        if (generateColor.get(z).get(i).type == 2){
                            block = false;
                            break block;
                        }else {
                            block = true;
                            Block[1] = true;
                        }
                    }
                }
            }
        }
        if (sum >= blockNumX * blockNumY * 2 / 3 || block)
            Strategy2();
    }

    public void Strategy2(){
        int[] playerX = {0, 0};
        int[] playerY = {0, 0};
        int[] comX = {0, 0};
        int[] comY = {0, 0};
        boolean[] greedy = {false, false};
        int appear = 0;
        int save1 = 0;
        int save2 = 0;
        int pytha = blockNumX + blockNumY;

        if (sum >= blockNumX * blockNumY * 2 / 3 || Block[0]) {
            //block top right road
            for (int j = 0; j <= (blockNumY - 1); j++) {
                for (int i = (blockNumX - 1) * 2 / 3; i <= blockNumX - 2; i++) {
                    if (generateColor.get(j).get(i).type == 1 && blockNumX - 1 - i + j < pytha) {
                        pytha = blockNumX - 1 - i + j;
                        playerX[0] = i;
                        playerY[0] = j;
                        appear = 1;
                    }
                }
            }
            if (appear == 1) {
                for (int z = blockNumX - 1 - playerX[0]; z > 0; z--) {
                    if (playerY[0] + z < blockNumY - 1) {
                        if (generateColor.get(playerY[0] + z).get(playerX[0] + z).type == 2) {
                            comX[0] = playerX[0] + z;
                            comY[0] = playerY[0] + z;
                            save1 = comY[0];
                            appear = 2;
                        }
                    }
                }
            }
            if (appear == 2) {
                for (int z = save1 - 1; z > 0; z--) {
                    if (generateColor.get(comY[0] - z).get(comX[0]).type == 2) {
                        comY[0] = comY[0] - z;
                        appear = 3;
                        if (z > 1)
                            greedy[0] = true;
                        break;
                    }
                }
            }
        }

        if (sum >= blockNumX * blockNumY * 2 / 3 || Block[1]) {
            //block bottom left road
            pytha = blockNumX + blockNumY;
            for (int j = (blockNumY - 1) * 2 / 3; j <= blockNumY - 2; j++) {
                for (int i = 0; i <= (blockNumX - 1); i++) {
                    if (generateColor.get(j).get(i).type == 1 && blockNumY - 1 - j + i < pytha) {
                        pytha = blockNumY - 1 - j + i;
                        playerX[1] = i;
                        playerY[1] = j;
                        appear = 4;
                    }
                }
            }
            if (appear == 4) {
                for (int z = blockNumX - 1 - playerX[1]; z > 0; z--) {
                    if (playerY[1] + z <= blockNumY - 1) {
                        if (generateColor.get(playerY[1] + z).get(playerX[1] + z).type == 2) {
                            comX[1] = playerX[1] + z;
                            comY[1] = playerY[1] + z;
                            save2 = comX[1];
                            appear = 5;
                        }
                    }
                }
            }
            if (appear == 5) {
                for (int z = save2 - 1; z > 0; z--) {
                    if (generateColor.get(comY[1]).get(comX[1] - z).type == 2) {
                        comX[1] = comX[1] - z;
                        appear = 6;
                        if (z > 1)
                            greedy[1] = true;
                        break;
                    }
                }
            }
        }

        if (comX[0] == 0 && comY[0] == 0 && comX[1] == 0 && comY[1] == 0){
            return;
        }
        if (comX[0] == 0 && comY[0] == 0){
            comX[0] = blockNumX;
            comY[0] = blockNumX;
        }
        else if (comX[1] == 0 && comY[1] == 0){
            comX[1] = blockNumX;
            comY[1] = blockNumX;
        }


        //choose the road that can block the most box
        if (appear != 0){
            if (comX[0] <= comY[1] &&
                    comY[0] - 1 >= 0 && comX[0] - 1 >= 0){
                if (greedy[0] && generateColor.get(comY[0]).get(comX[0] - 1).type == 0 &&
                        generateColor.get(comY[0]).get(comX[0] - 1).color != generateColor.get(0).get(0).color &&
                        generateColor.get(comY[0]).get(comX[0] - 1).color != generateColor.get(blockNumY - 1).get(blockNumX - 1).color)
                    cn1 = generateColor.get(comY[0]).get(comX[0] - 1).color;
                else if (generateColor.get(comY[0] - 1).get(comX[0]).type == 0 &&
                        generateColor.get(comY[0] - 1).get(comX[0]).color != generateColor.get(0).get(0).color &&
                        generateColor.get(comY[0] - 1).get(comX[0]).color != generateColor.get(blockNumY - 1).get(blockNumX - 1).color)
                    cn1 = generateColor.get(comY[0] - 1).get(comX[0]).color;
            }
            else if (comX[0] > comY[1] &&
                    comY[1] - 1 >= 0 && comX[1] + 1 <= blockNumX - 1){
                if (greedy[1] && generateColor.get(comY[1] - 1).get(comX[1]).type == 0 &&
                        generateColor.get(comY[1] - 1).get(comX[1]).color != generateColor.get(0).get(0).color &&
                        generateColor.get(comY[1] - 1).get(comX[1]).color != generateColor.get(blockNumY - 1).get(blockNumX - 1).color)
                    cn1 = generateColor.get(comY[1] - 1).get(comX[1]).color;
                else if (generateColor.get(comY[1]).get(comX[1] - 1).type == 0 &&
                        generateColor.get(comY[1]).get(comX[1] - 1).color != generateColor.get(0).get(0).color &&
                        generateColor.get(comY[1]).get(comX[1] - 1).color != generateColor.get(blockNumY - 1).get(blockNumX - 1).color)
                    cn1 = generateColor.get(comY[1]).get(comX[1] - 1).color;
            }
        }

        //completely block the road
       /* int[] block = {blockNum, blockNum};
        for (int z = blockNum - 1; z >= (blockNum - 1) * 2 / 3; z--){
            if (generateColor.get(1).get(z).type == 2 && generateColor.get(0).get(z).type == 0 &&
                    generateColor.get(0).get(z).color != generateColor.get(0).get(0).color &&
                    generateColor.get(0).get(z).color != generateColor.get(blockNumY - 1).get(blockNum - 1).color)
                block[0] = z;
            if (generateColor.get(z).get(1).type == 2 && generateColor.get(z).get(0).type == 0 &&
                    generateColor.get(z).get(0).color != generateColor.get(0).get(0).color &&
                    generateColor.get(z).get(0).color != generateColor.get(blockNumY - 1).get(blockNum - 1).color)
                block[1] = z;
        }

        if (block[0] != blockNum || block[1] != blockNum) {
            if (block[0] <= block[1])
                cn1 = generateColor.get(0).get(block[0]).color;
            else if (block[1] < block[0])
                cn1 = generateColor.get(block[1]).get(0).color;
        }*/
    }
}
