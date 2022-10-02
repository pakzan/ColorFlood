package com.github.pakzan.colorflood;

enum Owner {
    NO_ONE,
    AI,
    PLAYER_1,
    PLAYER_2,
    PLAYER_3,
    PLAYER_4;

    String getString() {
        switch (this) {
            case NO_ONE:
                return "No One";
            case AI:
                return "AI";
            case PLAYER_1:
                return "Player 1";
            case PLAYER_2:
                return "Player 2";
            case PLAYER_3:
                return "Player 3";
            case PLAYER_4:
                return "Player 4";
        }
        return "";
    }
}
