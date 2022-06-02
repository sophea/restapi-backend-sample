package com.jtrust.finetapi;

import io.awspring.cloud.autoconfigure.context.ContextInstanceDataAutoConfiguration;
import io.awspring.cloud.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = { ContextStackAutoConfiguration.class, ContextInstanceDataAutoConfiguration.class})
public class FinetApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(FinetApiApplication.class, args);
    }


}
