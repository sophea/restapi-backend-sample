package com.sma.backend.sqs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Callback {
  private String token;
  private String successUrl;
  private String failureUrl;
}
