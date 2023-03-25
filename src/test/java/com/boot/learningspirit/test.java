package com.boot.learningspirit;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @Project: learningSpirit
 * @Author: DengYinzhe
 * @Date: 2023/3/25 10:22
 * @FileName: test
 * @Description:
 */

public class test {

    @Test
    public static void main(String[] args) {

        String a = "123";
        String b = "123,456";

        System.out.println(Arrays.toString(a.split(",")));
    }
}
