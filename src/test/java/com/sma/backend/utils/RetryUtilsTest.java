package com.sma.backend.utils;

/**
 * Author: Mak Sophea
 * Date: 08/17/2021
 */
public class RetryUtilsTest {

    public static int value = 0;

    public static void main(String[] args) throws Exception {
        RetryUtilsTest obj = new RetryUtilsTest();
        String result = (String) RetryUtils.withRetryObject(5, 6000, obj::test);

        System.out.println(result);
    }

    public String test() throws Exception {
        if (value < 2) {
            value++;
            throw new Exception("sth wrong");
        }
        return "Value";
    }
}
