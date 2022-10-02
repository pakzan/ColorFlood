package com.github.pakzan.colorflood;

import androidx.core.util.Pair;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

/**
 * Created by Dell on 22/06/2015.
 */
public class AI {

    static GameState gameState = GameState.getInstance();

    static Color chooseColor(ColorBlock[][] colorPanel) {
        Color color;
        Owner[][] ownerPanel;
        Object2IntOpenHashMap<Color> aiAreaMap;
        Object2IntOpenHashMap<Color> playerAreaMap;

        switch (gameState.getMode()) {
            case EASY:
                color = maxAreaStrategy(colorPanel);
                if (color != null) {
                    return color;
                }
            case MEDIUM:
                ownerPanel = computeReflectedOwner(colorPanel);
                aiAreaMap = getSurfaceAreaMap(colorPanel, ownerPanel, Owner.AI);
                color = maxSurfaceAreaStrategy(colorPanel, aiAreaMap);
                if (color != null) {
                    return color;
                }

                color = maxAreaStrategy(colorPanel);
                if (color != null) {
                    return color;
                }
            case HARD:
                color = blockSideWayStrategy(colorPanel);
                if (color != null) {
                    return color;
                }

                ownerPanel = computeReflectedOwner(colorPanel);
                aiAreaMap = getSurfaceAreaMap(colorPanel, ownerPanel, Owner.AI);
                playerAreaMap = getSurfaceAreaMap(colorPanel, ownerPanel, Owner.PLAYER_1);
                color = maxNetSurfaceAreaStrategy(colorPanel, aiAreaMap, playerAreaMap);
                if (color != null) {
                    return color;
                }

                color = maxAreaStrategy(colorPanel);
                if (color != null) {
                    return color;
                }
        }
        return randomStrategy(colorPanel);
    }

    private static Color randomStrategy(ColorBlock[][] panel) {
        int rowNum = panel.length;
        int colNum = panel[0].length;
        Color playerColor = panel[0][0].getColor();
        Color aiColor = panel[rowNum - 1][colNum - 1].getColor();

        Color color;
        do {
            color = gameState.getColorNum().getRandomColor();
        }
        while (color == playerColor || color == aiColor);
        return color;
    }

    private static Color maxAreaStrategy(ColorBlock[][] panel) {
        long start = System.currentTimeMillis();

        int rowNum = panel.length;
        int colNum = panel[0].length;
        Color playerColor = panel[0][0].getColor();
        Color aiColor = panel[rowNum - 1][colNum - 1].getColor();

        // find color that consumes most number of boxes around the surface, without cascading
        // (ie: one big same-color region with multiple boxes might be counted fewer
        Object2ObjectOpenHashMap<Color, HashSet<Pair<Integer, Integer>>> colorAreaMap = new Object2ObjectOpenHashMap<>();
        for (Color color : gameState.getColorNum().getAllPlayableColors()) {
            colorAreaMap.put(color, new HashSet<>());
        }
        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                if (!panel[j][i].isOwner(Owner.AI)) {
                    continue;
                }
                if (i > 0) {
                    ColorBlock block = panel[j][i - 1];
                    if (block.isOwner(Owner.NO_ONE)) {
                        Objects.requireNonNull(colorAreaMap.get(block.getColor())).add(new Pair<>(i - 1, j));
                    }
                }
                if (i < colNum - 1) {
                    ColorBlock block = panel[j][i + 1];
                    if (block.isOwner(Owner.NO_ONE)) {
                        Objects.requireNonNull(colorAreaMap.get(block.getColor())).add(new Pair<>(i + 1, j));
                    }
                }
                if (j > 0) {
                    ColorBlock block = panel[j - 1][i];
                    if (block.isOwner(Owner.NO_ONE)) {
                        Objects.requireNonNull(colorAreaMap.get(block.getColor())).add(new Pair<>(i, j - 1));
                    }
                }
                if (j < rowNum - 1) {
                    ColorBlock block = panel[j + 1][i];
                    if (block.isOwner(Owner.NO_ONE)) {
                        Objects.requireNonNull(colorAreaMap.get(block.getColor())).add(new Pair<>(i, j + 1));
                    }
                }
            }
        }

        // cancel out color choice that is same with player/current color
        colorAreaMap.remove(playerColor);
        colorAreaMap.remove(aiColor);

        // get color with most number of boxes
        int maxArea = 0;
        Color maxColor = null;
        for (Map.Entry<Color, HashSet<Pair<Integer, Integer>>> colorSize : colorAreaMap.object2ObjectEntrySet()) {
            int size = colorSize.getValue().size();
            if (size > maxArea) {
                maxArea = size;
                maxColor = colorSize.getKey();
            }
        }

        timeLog(start);
        return maxColor;
    }

    private static Color maxSurfaceAreaStrategy(ColorBlock[][] colorPanel, Object2IntOpenHashMap<Color> aiAreaMap) {
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;
        Color playerColor = colorPanel[0][0].getColor();
        Color aiColor = colorPanel[rowNum - 1][colNum - 1].getColor();

        // get color with most number of surface area
        int maxArea = 0;
        Color maxColor = null;
        for (Map.Entry<Color, Integer> colorArea : aiAreaMap.object2IntEntrySet()) {
            if (colorArea.getKey() == aiColor || colorArea.getKey() == playerColor) {
                continue;
            }
            if (colorArea.getValue() > maxArea) {
                maxArea = colorArea.getValue();
                maxColor = colorArea.getKey();
            }
        }
        return maxColor;
    }

    private static Color maxNetSurfaceAreaStrategy(ColorBlock[][] colorPanel, Object2IntOpenHashMap<Color> aiAreaMap, Object2IntOpenHashMap<Color> playerAreaMap) {
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;
        Color playerColor = colorPanel[0][0].getColor();
        Color aiColor = colorPanel[rowNum - 1][colNum - 1].getColor();

        // get color with most number of net surface area (AI max surface area - player max surface area)
        int netMaxArea = 0;
        Color aiMaxColor = null;
        for (Map.Entry<Color, Integer> aiArea : aiAreaMap.object2IntEntrySet()) {
            if (aiArea.getKey() == aiColor || aiArea.getKey() == playerColor) {
                continue;
            }
            int playerMaxArea = 0;
            for (Map.Entry<Color, Integer> playerArea : playerAreaMap.object2IntEntrySet()) {
                if (playerArea.getKey() == aiColor || playerArea.getKey() == playerColor) {
                    continue;
                }
                if (playerArea.getValue() > playerMaxArea) {
                    playerMaxArea = playerArea.getValue();
                }
            }

            int netArea = aiArea.getValue() - playerMaxArea;
            if (netArea > netMaxArea) {
                netMaxArea = netArea;
                aiMaxColor = aiArea.getKey();
            }
        }
        return aiMaxColor;
    }

    private static Color blockSideWayStrategy(ColorBlock[][] colorPanel) {
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;
        Color playerColor = colorPanel[0][0].getColor();
        Color aiColor = colorPanel[rowNum - 1][colNum - 1].getColor();

        // block/surround the side-way so that all the boxes inside can be consumed safely
        // left side-way
        Pair<Integer, Integer> leftBlockPair = null;
        Pair<Integer, Integer> leftBlockedPair = null;
        outerLoop:
        for (int j = 0; j < rowNum; j++) {
            for (int i = colNum - 1; i >= (colNum - 1) * 2 / 3; i--) {
                if (colorPanel[j][i].isOwner(Owner.AI)) {
                    break;
                }
                if (colorPanel[j][i].isOwner(Owner.NO_ONE)) {
                    continue;
                }
                Pair<Integer, Integer> blockedPair = new Pair<>(i, j);
                Pair<Integer, Integer> blockPair = getBlockPair(colorPanel, blockedPair);
                if (blockPair != null) {
                    leftBlockedPair = blockedPair;
                    leftBlockPair = blockPair;
                    break outerLoop;
                }
                break;
            }
        }

        // right side-way
        // transform whole panel to left side-way to reuse the logic
        ColorBlock[][] transformPanel = new ColorBlock[colNum][rowNum];
        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                transformPanel[i][j] = colorPanel[j][i];
            }
        }

        Pair<Integer, Integer> bottomBlockedPair = null;
        Pair<Integer, Integer> bottomBlockPair = null;
        outerLoop:
        for (int j = 0; j < rowNum; j++) {
            for (int i = colNum - 1; i >= (colNum - 1) * 2 / 3; i--) {
                if (transformPanel[i][j].isOwner(Owner.AI)) {
                    break;
                }
                if (transformPanel[i][j].isOwner(Owner.NO_ONE)) {
                    continue;
                }

                Pair<Integer, Integer> blockedPair = new Pair<>(i, j);
                Pair<Integer, Integer> blockPair = getBlockPair(colorPanel, blockedPair);
                if (blockPair != null) {
                    bottomBlockedPair = new Pair<>(blockedPair.second, blockedPair.first);
                    bottomBlockPair = new Pair<>(blockPair.second, blockPair.first);
                    break outerLoop;
                }
                break;
            }
        }

        // block/surround the side-way with the most blocks
        if (leftBlockPair != null && (bottomBlockPair == null || bottomBlockPair.first * bottomBlockPair.second >= leftBlockPair.first * leftBlockPair.second)) {
            Color blockColor = colorPanel[leftBlockPair.second - 1][leftBlockPair.first].getColor();
            if (blockColor != playerColor) {
                return blockColor;
            }
            blockColor = colorPanel[leftBlockedPair.second][leftBlockedPair.first + 1].getColor();
            if (blockColor != playerColor && blockColor != aiColor) {
                return blockColor;
            }
        }

        if (bottomBlockPair != null) {
            Color blockColor = colorPanel[bottomBlockPair.second][bottomBlockPair.first - 1].getColor();
            if (blockColor == playerColor) {
                return blockColor;
            }
            blockColor = colorPanel[bottomBlockedPair.second + 1][bottomBlockedPair.first].getColor();
            if (blockColor != playerColor && blockColor != aiColor) {
                return blockColor;
            }
        }

        return null;
    }

    private static boolean surroundedBy(Owner[][] ownerPanel, boolean[][] visitedPanel, Pair<Integer, Integer> coordinate, Owner owner) {
        long start = System.currentTimeMillis();

        int rowNum = ownerPanel.length;
        int colNum = ownerPanel[0].length;

        LinkedList<Pair<Integer, Integer>> territory = new LinkedList<>() {
        };
        territory.add(coordinate);

        // check if a region is completely surrounded by the owner and/or corner
        while (!territory.isEmpty()) {
            Pair<Integer, Integer> block = territory.pop();
            int i = block.first;
            int j = block.second;

            if (visitedPanel[j][i]) {
                continue;
            }
            if (ownerPanel[j][i] == owner) {
                continue;
            }
            if (ownerPanel[j][i] != Owner.NO_ONE) {
                timeLog(start);
                return false;
            }
            visitedPanel[j][i] = true;

            if (i > 0) {
                territory.add(new Pair<>(i - 1, j));
            }
            if (i < colNum - 1) {
                territory.add(new Pair<>(i + 1, j));
            }
            if (j > 0) {
                territory.add(new Pair<>(i, j - 1));
            }
            if (j < rowNum - 1) {
                territory.add(new Pair<>(i, j + 1));
            }
        }
        timeLog(start);
        return true;
    }

    private static void spreadOwner(Owner[][] ownerPanel, Pair<Integer, Integer> coordinate, Owner owner) {
        long start = System.currentTimeMillis();

        int rowNum = ownerPanel.length;
        int colNum = ownerPanel[0].length;

        LinkedList<Pair<Integer, Integer>> territory = new LinkedList<>() {
        };
        territory.add(coordinate);

        while (!territory.isEmpty()) {
            Pair<Integer, Integer> block = territory.pop();
            int i = block.first;
            int j = block.second;

            if (ownerPanel[j][i] != Owner.NO_ONE) {
                continue;
            }
            ownerPanel[j][i] = owner;

            if (i > 0) {
                territory.add(new Pair<>(i - 1, j));
            }
            if (i < colNum - 1) {
                territory.add(new Pair<>(i + 1, j));
            }
            if (j > 0) {
                territory.add(new Pair<>(i, j - 1));
            }
            if (j < rowNum - 1) {
                territory.add(new Pair<>(i, j + 1));
            }
        }
        timeLog(start);
    }

    private static int countSurfaceArea(ColorBlock[][] colorPanel, Owner owner) {
        HashSet<Pair<Integer, Integer>> area = new HashSet<>();
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;

        // count surface area(without cascading) + new added territory
        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                if (colorPanel[j][i].isOwner(owner)) {
                    area.add(new Pair<>(i, j));
                    if (i > 0) {
                        if (colorPanel[j][i - 1].isOwner(Owner.NO_ONE)) {
                            area.add(new Pair<>(i - 1, j));
                        }
                    }
                    if (i < colNum - 1) {
                        if (colorPanel[j][i + 1].isOwner(Owner.NO_ONE)) {
                            area.add(new Pair<>(i + 1, j));
                        }
                    }
                    if (j > 0) {
                        if (colorPanel[j - 1][i].isOwner(Owner.NO_ONE)) {
                            area.add(new Pair<>(i, j - 1));
                        }
                    }
                    if (j < rowNum - 1) {
                        if (colorPanel[j + 1][i].isOwner(Owner.NO_ONE)) {
                            area.add(new Pair<>(i, j + 1));
                        }
                    }
                }
            }
        }
        return area.size();
    }

    private static Object2IntOpenHashMap<Color> getSurfaceAreaMap(ColorBlock[][] colorPanel, Owner[][] ownerPanel, Owner owner) {
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;

        Object2IntOpenHashMap<Color> colorAreaMap = new Object2IntOpenHashMap<>();
        colorAreaMap.defaultReturnValue(0);

        ColorBlock[][] testPanel = new ColorBlock[rowNum][colNum];
        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                testPanel[j][i] = new ColorBlock(ownerPanel[j][i], colorPanel[j][i].getColor());
            }
        }
        int currSurfaceArea = countSurfaceArea(testPanel, owner);

        for (Color color : gameState.getColorNum().getAllPlayableColors()) {
            testPanel = new ColorBlock[rowNum][colNum];
            for (int j = 0; j < rowNum; j++) {
                for (int i = 0; i < colNum; i++) {
                    testPanel[j][i] = new ColorBlock(ownerPanel[j][i], colorPanel[j][i].getColor());
                }
            }
            ColorUtils.spreadColor(testPanel, owner, color);
            colorAreaMap.put(color, countSurfaceArea(testPanel, owner) - currSurfaceArea);
        }
        return colorAreaMap;
    }

    private static Pair<Integer, Integer> getBlockPair(ColorBlock[][] colorPanel, Pair<Integer, Integer> player) {
        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;

        // check if a player territory from the left side-way can be blocked by AI
        int yDist = 1;
        for (int j = player.second + 1; j < rowNum; j++) {
            for (int i = player.first + yDist; i < colNum; i++) {
                if (colorPanel[j][i].isOwner(Owner.AI)) {
                    return new Pair<>(i, j);
                }
            }
            yDist += 1;
        }
        return null;
    }

    private static Owner[][] computeReflectedOwner(ColorBlock[][] colorPanel) {
        long start = System.currentTimeMillis();

        int rowNum = colorPanel.length;
        int colNum = colorPanel[0].length;

        Owner[][] ownerPanel = new Owner[rowNum][colNum];
        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                ownerPanel[j][i] = colorPanel[j][i].getOwner();
            }
        }
        boolean[][] visitedPanel = new boolean[rowNum][colNum];

        // For each box, dfs to see the region has already surrounded by AI/Player
        // If yes, mark all boxes in the region to the respective owner
        for (int j = 0; j < rowNum; j++) {
            for (int i = 0; i < colNum; i++) {
                Pair<Integer, Integer> p = new Pair<>(i, j);
                if (ownerPanel[j][i] == Owner.NO_ONE && surroundedBy(ownerPanel, visitedPanel, p, Owner.AI)) {
                    spreadOwner(ownerPanel, p, Owner.AI);
                }
                if (ownerPanel[j][i] == Owner.NO_ONE && surroundedBy(ownerPanel, visitedPanel, p, Owner.PLAYER_1)) {
                    spreadOwner(ownerPanel, p, Owner.PLAYER_1);
                }
            }
        }
        timeLog(start);

        return ownerPanel;
    }

    static void timeLog(long startMillis) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[3];

        Logger.getLogger("AI").log(Level.INFO, String.format(Locale.ENGLISH, "%s took: %d ms", e.getMethodName(), System.currentTimeMillis() - startMillis));
    }
}
