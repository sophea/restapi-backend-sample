package com.sma.backend.sqs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Resource {

    private String id;

    private String fileName;

    private String archiveFilename;

}
