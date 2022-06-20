package com.sma.backend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HelloController {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Logger logger = LoggerFactory.getLogger(HelloController.class);


    @GetMapping("/hello")
    @SwaggerPublicApi
    public String hello(@RequestParam(value = "name", defaultValue = "FINET API") String name) {
        log.info("=========hello===========");

        this.logger.debug("=========DEBUG===========");
        this.logger.info("=========INFO===========");
        this.logger.warn("=========WARNING==========");
        this.logger.error("=========ERROR===========");

        return String.format("Hello %s!", name);
    }


    @SwaggerPublicApi
    @GetMapping("/api/myaccount/balance")
    public Map<String, Object> getMyBalance() {
        log.info("=========getMyBalanace===========");
        final Map<String, Object> result = new HashMap<>();

        result.put("cif", "000000000");
        result.put("accountName", "JTRB Testing");
        result.put("balance", 134.53d);
        result.put("currency", "USD");
        result.put("status", 1);

        log.info(String.format("result output %s", this.gson.toJson(result)));
        return result;
    }

    @SwaggerPublicApi
    @PostMapping("/api/myaccount/balance/update")
    public Map<String, Object> updateMyBalance(String cif, String account, Double amount, String ref) {
        log.info("=========updateMyBalanace===========");
        final Map<String, Object> result = new HashMap<>();

        result.put("cif", cif);
        result.put("accountName", account);
        result.put("balance", amount);
        result.put("currency", "USD");
        result.put("status", 1);
        result.put("ref", ref);
        log.info(String.format("result output %s", this.gson.toJson(result)));
        return result;
    }
}
