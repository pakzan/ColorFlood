package com.github.pakzan.colorflood;

import androidx.core.util.Pair;

import java.util.HashSet;
import java.util.Iterator;

class ColorUtils {

    static void spreadColor(ColorBlock[][] colorPanel, Owner owner, Color color) {
        HashSet<Pair<Integer, Integer>> territory = new HashSet<>();
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;

        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                if (colorPanel[j][i].isOwner(owner)) {
                    colorPanel[j][i].setColor(color);
                    territory.add(new Pair<>(i, j));
                }
            }
        }
        while (!territory.isEmpty()) {
            Iterator<Pair<Integer, Integer>> iterator = territory.iterator();
            Pair<Integer, Integer> block = iterator.next();
            iterator.remove();
            int i = block.first;
            int j = block.second;

            if (i > 0) {
                if (colorPanel[j][i - 1].isOwner(Owner.NO_ONE) &&
                        colorPanel[j][i - 1].isColor(color)) {
                    colorPanel[j][i - 1].setOwner(owner);
                    territory.add(new Pair<>(i - 1, j));
                }
            }
            if (i < colNum - 1) {
                if (colorPanel[j][i + 1].isOwner(Owner.NO_ONE) &&
                        colorPanel[j][i + 1].isColor(color)) {
                    colorPanel[j][i + 1].setOwner(owner);
                    territory.add(new Pair<>(i + 1, j));
                }
            }
            if (j > 0) {
                if (colorPanel[j - 1][i].isOwner(Owner.NO_ONE) &&
                        colorPanel[j - 1][i].isColor(color)) {
                    colorPanel[j - 1][i].setOwner(owner);
                    territory.add(new Pair<>(i, j - 1));
                }
            }
            if (j < rowNum - 1) {
                if (colorPanel[j + 1][i].isOwner(Owner.NO_ONE) &&
                        colorPanel[j + 1][i].isColor(color)) {
                    colorPanel[j + 1][i].setOwner(owner);
                    territory.add(new Pair<>(i, j + 1));
                }
            }
        }
    }
}
