package com.github.pakzan.colorflood;

public class GameState {
    private PlayType type = PlayType.WITH_AI;
    private PlayMode mode = PlayMode.EASY;
    private ColorNum colorNum = ColorNum.SIX;
    private int boxNumX = 10;
    private int boxNumY = 10;
    private int brightness = 60;

    private static final GameState state = new GameState();

    public static GameState getInstance() {
        return state;
    }

    public PlayType getType() {
        return type;
    }

    public void setType(PlayType type) {
        this.type = type;
    }

    public PlayMode getMode() {
        return mode;
    }

    public void setMode(PlayMode mode) {
        this.mode = mode;
    }

    public ColorNum getColorNum() {
        return colorNum;
    }

    public void setColorNum(ColorNum colorNum) {
        this.colorNum = colorNum;
    }

    public int getBoxNumX() {
        return boxNumX;
    }

    public void setBoxNumX(int boxNumX) {
        this.boxNumX = boxNumX;
    }

    public int getBoxNumY() {
        return boxNumY;
    }

    public void setBoxNumY(int boxNumY) {
        this.boxNumY = boxNumY;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }
}
