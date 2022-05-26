package com.jtrust.finetapi.sqs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueuePayload {

    private String source;
    private String destination;
    private String uuid;
}
