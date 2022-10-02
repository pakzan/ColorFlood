package com.github.pakzan.colorflood;

import android.annotation.SuppressLint;

import androidx.annotation.IdRes;

enum PlayMode {
    EASY,
    MEDIUM,
    HARD;

    @SuppressLint("NonConstantResourceId")
    static PlayMode getPlayMode(@IdRes int viewId) {
        switch (viewId) {
            case R.id.rb_easy:
                return EASY;
            case R.id.rb_medium:
                return MEDIUM;
            case R.id.rb_hard:
                return HARD;
        }
        return null;
    }

    @IdRes
    int getViewId() {
        switch (this) {
            case EASY:
                return R.id.rb_easy;
            case MEDIUM:
                return R.id.rb_medium;
            case HARD:
                return R.id.rb_hard;
        }
        return -1;
    }
}
