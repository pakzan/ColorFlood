package com.github.pakzan.colorflood;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.IdRes;
import androidx.core.content.ContextCompat;

/**
 * Created by Dell on 22/06/2015.
 */

enum Color {
    Neutral,
    One,
    Two,
    Three,
    Four,
    Five,
    Six,
    Seven,
    Eight,
    Nine,
    Ten;

    @SuppressLint("NonConstantResourceId")
    static Color getColor(@IdRes int viewId) {
        switch (viewId) {
            case R.id.btn_color1:
                return One;
            case R.id.btn_color2:
                return Two;
            case R.id.btn_color3:
                return Three;
            case R.id.btn_color4:
                return Four;
            case R.id.btn_color5:
                return Five;
            case R.id.btn_color6:
                return Six;
            case R.id.btn_color7:
                return Seven;
            case R.id.btn_color8:
                return Eight;
            case R.id.btn_color9:
                return Nine;
            case R.id.btn_color10:
                return Ten;
        }
        return null;
    }

    @IdRes
    int getViewId() {
        switch (this) {
            case One:
                return R.id.btn_color1;
            case Two:
                return R.id.btn_color2;
            case Three:
                return R.id.btn_color3;
            case Four:
                return R.id.btn_color4;
            case Five:
                return R.id.btn_color5;
            case Six:
                return R.id.btn_color6;
            case Seven:
                return R.id.btn_color7;
            case Eight:
                return R.id.btn_color8;
            case Nine:
                return R.id.btn_color9;
            case Ten:
                return R.id.btn_color10;
        }
        return -1;
    }

    int getColorCode(Context context) {
        int colorAttr;
        switch (this) {
            case Neutral:
                colorAttr = R.color.colorNeutral;
                break;
            case One:
                colorAttr = R.color.colorOne;
                break;
            case Two:
                colorAttr = R.color.colorTwo;
                break;
            case Three:
                colorAttr = R.color.colorThree;
                break;
            case Four:
                colorAttr = R.color.colorFour;
                break;
            case Five:
                colorAttr = R.color.colorFive;
                break;
            case Six:
                colorAttr = R.color.colorSix;
                break;
            case Seven:
                colorAttr = R.color.colorSeven;
                break;
            case Eight:
                colorAttr = R.color.colorEight;
                break;
            case Nine:
                colorAttr = R.color.colorNine;
                break;
            case Ten:
                colorAttr = R.color.colorTen;
                break;
            default:
                return -1;
        }
        return ContextCompat.getColor(context, colorAttr);
    }
}
