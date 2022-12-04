package com.ys.librarymanagement.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime timeStamp;

    private int status;

    private String message;

    private String requestUrl;

    @Builder
    public ErrorResponse(LocalDateTime timeStamp, int status, String message,
        String requestUrl) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.message = message;
        this.requestUrl = requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
