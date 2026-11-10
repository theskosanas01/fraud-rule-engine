package com.fraud.engine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Standard API error response")
public class ApiErrorResponse {

    @Schema(description = "HTTP status code", example = "400")
    private Integer status;

    @Schema(description = "Error message", example = "Invalid request parameters")
    private String message;

    @Schema(description = "Error timestamp", example = "2024-01-15T10:35:00")
    private LocalDateTime timestamp;

    @Schema(description = "Request path that caused the error", example = "/api/transactions")
    private String path;

    @Schema(description = "Detailed error information")
    private String details;

    // Constructors
    public ApiErrorResponse() {
    }

    public ApiErrorResponse(Integer status, String message, LocalDateTime timestamp, String path, String details) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.details = details;
    }

    // Getters and Setters
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer status;
        private String message;
        private LocalDateTime timestamp;
        private String path;
        private String details;

        public Builder status(Integer status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder details(String details) {
            this.details = details;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(status, message, timestamp, path, details);
        }
    }
}
