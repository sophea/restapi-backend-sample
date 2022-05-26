package com.jtrust.finetapi.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.jtrust.finetapi.sqs.QueuePayload;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@ConditionalOnProperty(name = "aws.sqs.enabled", havingValue = "true")
public class QueueSqsService {

    private final QueueMessagingTemplate queueMessagingTemplate;

    @Value("${aws.sqs.queueName}")
    private String queueName;

    @Autowired
    public QueueSqsService(AmazonSQSAsync amazonSQSAsync) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSQSAsync);
    }

    public void send(QueuePayload message) {
        this.queueMessagingTemplate.send(queueName, MessageBuilder.withPayload(message).build());
    }

    @SqsListener(value = "${aws.sqs.queueName}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void receiveMessage(QueuePayload payload, @Header("header") String sendId) {
        log.info("### Received message {}, with senderId {} ", payload, sendId);
    }
}
