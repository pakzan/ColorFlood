package com.github.pakzan.colorflood;

/**
 * Created by LabAdmin on 16/6/2015.
 */
public class ColorBlock {
    private Owner owner = Owner.NO_ONE;
    private Color color;

    static GameState gameState = GameState.getInstance();

    public ColorBlock() {
        setColor(gameState.getColorNum().getRandomColor());
    }

    public ColorBlock(Owner owner, Color color) {
        this.setColor(color);
        this.setOwner(owner);
    }

    public boolean isColor(Color color) {
        return this.color == color;
    }

    public boolean isOwner(Owner owner) {
        return this.owner == owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Owner getOwner() {
        return owner;
    }

    public Color getColor() {
        return color;
    }
}
