package com.sma.backend;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "error response")
public class ErrorResponse {
    private final String errorCode;
    private final String message;

    public ErrorResponse(String code, String message) {
        this.errorCode = code;
        this.message = message;
    }

    @ApiModelProperty("Doc API defined error code, to mark different error Scenarios")
    public String getErrorCode() {
        return this.errorCode;
    }

    @ApiModelProperty("description of the error scenario")
    public String getMessage() {
        return this.message;
    }
}
