package com.sma.backend;

import com.sma.backend.service.QueueSqsService;
import com.sma.backend.sqs.QueuePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * sqs rest-api controller.
 * @author Sophea Mak
 * start with /api
 */
@RestController
@Slf4j
@ConditionalOnProperty(name = "aws.sqs.enabled", havingValue = "true")
public class SqsController {
  private final QueueSqsService service;

  SqsController(final QueueSqsService service) {
    this.service = service;
  }

  @PostMapping("api/send/message")
  @SwaggerPublicApi
  public QueuePayload sendPayload(@RequestBody QueuePayload payload) {
    log.info("=========sendPayload=========== {} ", payload);
    this.service.send(payload);
    return payload;
  }
}
