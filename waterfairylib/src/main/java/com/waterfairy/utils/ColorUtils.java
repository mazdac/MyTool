package com.waterfairy.utils;

import android.graphics.Color;

import java.util.Random;

/**
 * Created by water_fairy on 2017/6/5.
 * 995637517@qq.com
 */

public class ColorUtils {
    public static int randomColor() {
        Random random = new Random();
        return Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }
}
