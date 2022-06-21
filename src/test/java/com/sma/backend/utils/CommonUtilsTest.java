/*
 * Copyright (c) 2022.
 */

package com.sma.backend.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Sophea Mak
 * @since June-2022
 */
class CommonUtilsTest {

    @BeforeEach
    void setUp() {}

    @Test
    void base64Zip() {
        String content = "testValue";
        InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
        String value = CommonUtils.base64Zip(stream, "test.zip");
        Assertions.assertNotNull(value);
    }
}
