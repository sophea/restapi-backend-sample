/*
 * Copyright (c) 2022.
 */

package com.sma.backend.controller;

import com.sma.backend.config.SwaggerPublicApi;
import com.sma.backend.service.QueueSqsService;
import com.sma.backend.sqs.QueuePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * sqs rest-api controller.
 *
 * @author Sophea Mak start with /api
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@ConditionalOnProperty(name = "aws.sqs.enabled", havingValue = "true")
public class SqsController {

    private final QueueSqsService service;

    @PostMapping("api/send/message")
    @SwaggerPublicApi
    public QueuePayload sendPayload(@RequestBody QueuePayload payload) {
        log.info("=========sendPayload=========== {} ", payload);
        this.service.send(payload);
        return payload;
    }

}
