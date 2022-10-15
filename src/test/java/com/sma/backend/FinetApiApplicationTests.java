package com.sma.backend;

import com.sma.backend.controller.HelloController;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class FinetApiApplicationTests {

    @Autowired
    private HelloController controller;

    @Test
    void contextLoads() {
        Assertions.assertThat(controller).isNotNull();
    }

}
