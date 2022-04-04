package com.jtrust.finetapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HelloController {
    @GetMapping("/hello")
    @SwaggerPublicApi
    public String hello(@RequestParam(value = "name", defaultValue = "FINET API") String name) {
        return String.format("Hello %s!", name);
    }


    @SwaggerPublicApi
    @GetMapping("/api/myaccount/balance")
    public Map<String, Object> getMyBalanace() {

        final Map<String, Object> result = new HashMap<>();

        result.put("cif", "000000000");
        result.put("accountName", "JTRB Testing");
        result.put("balance", 134.53d);
        result.put("currency", "USD");
        result.put("status", 1);
        return result;
    }
}
