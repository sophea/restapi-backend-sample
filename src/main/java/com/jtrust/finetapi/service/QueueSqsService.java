package com.jtrust.finetapi.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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

import java.util.Map;

@Slf4j
@Service
@ConditionalOnProperty(name = "aws.sqs.enabled", havingValue = "true")
public class QueueSqsService {

    private final QueueMessagingTemplate queueMessagingTemplate;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Value("${aws.sqs.queueName}")
    private String queueName;

    @Autowired
    public QueueSqsService(AmazonSQSAsync amazonSQSAsync) {
        this.queueMessagingTemplate = new QueueMessagingTemplate(amazonSQSAsync);
    }

    /**
     * @param payload
     * {
     *     "source": "s3",
     *     "destination": "netApp",
     *     "uuid": "2342343243243sfaffasdfdsf"
     * }
     */
    public void send(QueuePayload payload) {
        final String message = gson.toJson(payload);
        log.info("sending message {} ", message);
        this.queueMessagingTemplate.convertAndSend(queueName, payload);
    }

    @SqsListener(value = "${aws.sqs.queueName}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void receiveMessage(QueuePayload payload, @Header("headers") Map<String, String> headers) {
        log.info("### Received message {}, with senderId {} ", gson.toJson(payload), gson.toJson(headers));
    }
}
