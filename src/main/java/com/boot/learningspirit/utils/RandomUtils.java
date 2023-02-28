package com.boot.learningspirit.utils;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/31 13:43
 * @FileName: RandomUtils
 * @Description: 六位数短信验证码随机生成
 */
public class RandomUtils {
    private static final Random random = new Random();

    private static final DecimalFormat sixdf = new DecimalFormat("000000");

    public static String getSixBitRandom() {
        return sixdf.format(random.nextInt(1000000));
    }
}
