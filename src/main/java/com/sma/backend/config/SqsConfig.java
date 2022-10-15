package com.sma.backend.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import java.util.Collections;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

/**
 * Sqs configuration.
 *
 * @author Sophea Mak
 * @since 2022
 */
@Configuration
@ConditionalOnProperty(name = "aws.sqs.enabled", havingValue = "true")
@Profile("!(local | test)")
public class SqsConfig {

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean(name = "amazonSqsAsync")
    @Primary
    public AmazonSQSAsync amazonSqsAsync() {
        return AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1)
                .withCredentials(new DefaultAWSCredentialsProviderChain()).build();
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory(final ObjectMapper mapper,
            final AmazonSQSAsync amazonSQSAsync) {
        final QueueMessageHandlerFactory queueHandlerFactory = new QueueMessageHandlerFactory();
        queueHandlerFactory.setAmazonSqs(amazonSQSAsync);
        queueHandlerFactory.setArgumentResolvers(
                Collections.singletonList(new PayloadMethodArgumentResolver(jackson2MessageConverter(mapper))));
        return queueHandlerFactory;
    }

    private MessageConverter jackson2MessageConverter(final ObjectMapper mapper) {
        final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setStrictContentTypeMatch(false);
        converter.setObjectMapper(mapper);
        return converter;
    }

}
