package com.github.pakzan.colorflood;

import java.util.ArrayList;
import java.util.List;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

class WinState {
    private final Object2IntOpenHashMap<Owner> territorySizes;
    private List<Owner> winners = new ArrayList<>();
    private int winnerSize = 0;

    WinState(ColorBlock[][] colorPanel) {
        Object2IntOpenHashMap<Owner> territorySizes = new Object2IntOpenHashMap<>();
        territorySizes.defaultReturnValue(0);

        for (ColorBlock[] rowBlock : colorPanel) {
            for (ColorBlock block : rowBlock) {
                territorySizes.addTo(block.getOwner(), 1);
            }
        }
        this.territorySizes = territorySizes;

        if (territorySizes.getOrDefault(Owner.NO_ONE, 0) > 0) return;

        territorySizes.forEach((owner, size) -> {
            if (size > winnerSize) {
                winners = new ArrayList<>() {{
                    add(owner);
                }};
                winnerSize = size;
            } else if (size == winnerSize) {
                winners.add(owner);
            }
        });
    }

    boolean hasGameOver() {
        return winners.size() > 0;
    }

    List<String> getWinners() {
        List<String> winnerStr = new ArrayList<>();
        winners.forEach(owner -> winnerStr.add(owner.getString()));
        return winnerStr;
    }

    int getScore(Owner owner) {
        return territorySizes.getInt(owner);
    }
}
