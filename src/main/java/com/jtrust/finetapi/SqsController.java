package com.jtrust.finetapi;

import com.jtrust.finetapi.service.QueueSqsService;
import com.jtrust.finetapi.sqs.QueuePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@ConditionalOnProperty(name="aws.sqs.enabled", havingValue="true")
public class SqsController {

    private QueueSqsService service;
    @PostMapping("api/send/message")
    @SwaggerPublicApi
    public QueuePayload sendPayload(@RequestBody QueuePayload payload) {
        log.info("=========sendPayload=========== {} ", payload);
        service.send(payload);
        return payload;
    }


}
