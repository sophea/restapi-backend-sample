package com.jtrust.finetapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Map;

@SpringBootApplication
@Slf4j
public class FinetApiApplication {

    @Value("#{${aws.secret.manager}}")
    private Map<String, String> awsConfigProperties;

    @Value("${aws.secret.manager}")
    private String awsConfigPropertiesString;


    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    public static void main(String[] args) {
        SpringApplication.run(FinetApiApplication.class, args);


    }

    @Bean
    public void testBean(){
        log.info("=========LOADING AWS SECRET MANAGER CONFIG=============");
        log.info("String object {}", awsConfigPropertiesString);
        log.info(gson.toJson(awsConfigProperties));
    }
}
