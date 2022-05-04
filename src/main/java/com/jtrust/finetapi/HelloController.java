package com.jtrust.finetapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.java.Log;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log
public class HelloController {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();


    @GetMapping("/hello")
    @SwaggerPublicApi
    public String hello(@RequestParam(value = "name", defaultValue = "FINET API") String name) {
        log.info("=========hello===========");
        return String.format("Hello %s!", name);
    }


    @SwaggerPublicApi
    @GetMapping("/api/myaccount/balance")
    public Map<String, Object> getMyBalanace() {
        log.info("=========getMyBalanace===========");

        final Map<String, Object> result = new HashMap<>();

        result.put("cif", "000000000");
        result.put("accountName", "JTRB Testing");
        result.put("balance", 134.53d);
        result.put("currency", "USD");
        result.put("status", 1);

        log.info(String.format("result output %s", gson.toJson(result)));
        return result;
    }

    @SwaggerPublicApi
    @PostMapping("/api/myaccount/balance/update")
    public Map<String, Object> updateMyBalanace(String cif, String account, Double amount, String ref) {
        log.info("=========updateMyBalanace===========");
        final Map<String, Object> result = new HashMap<>();

        result.put("cif", cif);
        result.put("accountName", "JTRB Testing");
        result.put("balance", amount);
        result.put("currency", "USD");
        result.put("status", 1);
        result.put("ref", ref);
        log.info(String.format("result output %s", gson.toJson(result)));
        return result;
    }
}
