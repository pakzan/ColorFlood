package com.github.pakzan.colorflood;

import android.annotation.SuppressLint;

import androidx.annotation.IdRes;

enum PlayType {
    WITH_AI,
    TWO_PLAYERS,
    FOUR_PLAYERS;

    @SuppressLint("NonConstantResourceId")
    static PlayType getPlayType(@IdRes int viewId) {
        switch (viewId) {
            case R.id.rb_with_ai:
                return WITH_AI;
            case R.id.rb_two_players:
                return TWO_PLAYERS;
            case R.id.rb_four_players:
                return FOUR_PLAYERS;
        }
        return null;
    }

    @IdRes
    int getViewId() {
        switch (this) {
            case WITH_AI:
                return R.id.rb_with_ai;
            case TWO_PLAYERS:
                return R.id.rb_two_players;
            case FOUR_PLAYERS:
                return R.id.rb_four_players;
        }
        return -1;
    }
}
