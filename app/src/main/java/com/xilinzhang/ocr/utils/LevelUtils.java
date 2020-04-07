package com.xilinzhang.ocr.utils;

public class LevelUtils {
    public static int level;
    public static int exp;

    public static int getNewLevelExp() {
        return (level + 1) * (level + 1);
    }

    public static void addExpWhenSearch() {
        exp += 2;
        ifUpgrade();
        updateExp();
    }

    public static void addExpWhenPutUpQuestion() {
        exp += 4;
        ifUpgrade();
        updateExp();
    }

    public static void addExpWhenDoQuestion() {
        exp += 8;
        ifUpgrade();
        updateExp();
    }

    public static void addExpDaily() {
        exp += 2;
        ifUpgrade();
        updateExp();
    }

    private static void ifUpgrade() {
        if (exp >= getNewLevelExp()) {
            exp -= getNewLevelExp();
            level += 1;
            //TODO some animation
            ifUpgrade();
        }
    }

    private static void updateExp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkUtils.setExp();
            }
        }).start();
    }
}
