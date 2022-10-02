package com.github.pakzan.colorflood;

import android.annotation.SuppressLint;

import androidx.annotation.IdRes;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Random;

enum ColorNum {
    SIX,
    TEN;

    @SuppressLint("NonConstantResourceId")
    static ColorNum getColorNum(@IdRes int viewId) {
        switch (viewId) {
            case R.id.rb_six_colors:
                return SIX;
            case R.id.rb_ten_colors:
                return TEN;
        }
        return null;
    }

    @IdRes
    int getViewId() {
        switch (this) {
            case SIX:
                return R.id.rb_six_colors;
            case TEN:
                return R.id.rb_ten_colors;
        }
        return -1;
    }

    private static final Random rand = new SecureRandom();

    Color getRandomColor() {
        switch (this) {
            case SIX:
                return Color.values()[rand.nextInt(6) + 1]; // index 1 to 6
            case TEN:
                return Color.values()[rand.nextInt(10) + 1]; // index 1 to 10
        }
        return null;
    }

    Color[] getAllPlayableColors() {
        switch (this) {
            case SIX:
                return Arrays.copyOfRange(Color.values(), 1, 7);
            case TEN:
                return Arrays.copyOfRange(Color.values(), 1, 11);
        }
        return new Color[]{};
    }
}
