package com.sma.backend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Author: Mak Sophea
 * Date: 08/17/2021
 */
class RetryUtilsTest {

    public static int value = 0;

    public static void main(String[] args) throws Exception {
        RetryUtilsTest obj = new RetryUtilsTest();
        String result = (String) RetryUtils.withRetryObject(3, 6000, obj::test);

        System.out.println(result);
    }

    @Test
    void testWithRetryObject() throws Exception {
        String result = (String) RetryUtils.withRetryObject(3, 3000, this::test);
        Assertions.assertEquals("Value", result);


    }

    @Test
    void testRetry() throws Exception {
        boolean result = RetryUtils.withRetry(3, 1000, this::testBoolean);
        Assertions.assertEquals(true, result);
    }

    String test() throws Exception {
        if (value < 2) {
            value++;
            throw new Exception("sth wrong");
        }
        return "Value";
    }

    public boolean testBoolean() {

        return true;
    }
}
