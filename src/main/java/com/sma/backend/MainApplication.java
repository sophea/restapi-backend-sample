package com.sma.backend;

import io.awspring.cloud.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import io.awspring.cloud.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * SpringBootApplication main application.
 *
 * @author Sophea Mak
 */
@SpringBootApplication(exclude = {ContextStackAutoConfiguration.class, ContextInstanceDataAutoConfiguration.class})
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }
}
