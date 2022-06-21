package com.sma.backend.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sma.backend.sqs.QueuePayload;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

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
     * send method.
     *
     * @param payload { "source": "s3", "destination": "netApp", "uuid": "2342343243243sfaffasdfdsf"
     *     }
     */
    public void send(QueuePayload payload) {
        final String message = this.gson.toJson(payload);
        log.info("sending message {} ", message);
        this.queueMessagingTemplate.convertAndSend(this.queueName, payload);
    }

    @SqsListener(value = "${aws.sqs.queueName}", deletionPolicy = SqsMessageDeletionPolicy.ALWAYS)
    public void receiveMessage(
            QueuePayload payload, @Header("headers") Map<String, String> headers) {
        log.info(
                "### Received message {}, with senderId {} ",
                this.gson.toJson(payload),
                this.gson.toJson(headers));
    }
}
