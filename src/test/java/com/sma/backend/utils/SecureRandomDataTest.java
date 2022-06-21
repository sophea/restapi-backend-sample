package com.sma.backend.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** Author: Mak Sophea Date: 08/07/2020 */
class SecureRandomDataTest {

    @Test
    void testRandomString() {

        Assertions.assertEquals(10, SecureRandomDataUtils.getString(10).length());

        Assertions.assertNotNull(SecureRandomDataUtils.getRandomNumber(10));

        Assertions.assertNotNull(SecureRandomDataUtils.getString(5, 10));
    }
}
