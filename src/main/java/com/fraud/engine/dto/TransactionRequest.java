package com.fraud.engine.dto;

import com.fraud.engine.entity.TransactionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Request DTO for creating a new transaction")
public class TransactionRequest {

    @NotBlank(message = "Transaction ID is required")
    @Size(max = 50, message = "Transaction ID must not exceed 50 characters")
    @Schema(description = "Unique transaction identifier", example = "TXN-2024-001")
    private String transactionId;

    @NotBlank(message = "Customer ID is required")
    @Size(max = 50, message = "Customer ID must not exceed 50 characters")
    @Schema(description = "Customer identifier", example = "CUST-12345")
    private String customerId;

    @NotBlank(message = "Merchant ID is required")
    @Size(max = 50, message = "Merchant ID must not exceed 50 characters")
    @Schema(description = "Merchant identifier", example = "MERCH-67890")
    private String merchantId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @DecimalMax(value = "999999.99", message = "Amount must not exceed 999999.99")
    @Schema(description = "Transaction amount", example = "150.50")
    private BigDecimal amount;

    @NotBlank(message = "Currency is required")
    @Size(min = 3, max = 3, message = "Currency must be a 3-letter ISO code")
    @Schema(description = "ISO 4217 currency code", example = "USD")
    private String currency;

    @NotNull(message = "Category is required")
    @Schema(description = "Transaction category", example = "ONLINE_PURCHASE")
    private TransactionCategory category;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Transaction description", example = "Electronics purchase")
    private String description;

    @NotNull(message = "Transaction timestamp is required")
    @Schema(description = "When the transaction occurred", example = "2024-01-15T10:30:00")
    private LocalDateTime transactionTimestamp;

    // Constructors
    public TransactionRequest() {
    }

    public TransactionRequest(String transactionId, String customerId, String merchantId,
                             BigDecimal amount, String currency, TransactionCategory category,
                             String description, LocalDateTime transactionTimestamp) {
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.description = description;
        this.transactionTimestamp = transactionTimestamp;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public void setCategory(TransactionCategory category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(LocalDateTime transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String transactionId;
        private String customerId;
        private String merchantId;
        private BigDecimal amount;
        private String currency;
        private TransactionCategory category;
        private String description;
        private LocalDateTime transactionTimestamp;

        public Builder transactionId(String transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        public Builder customerId(String customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder merchantId(String merchantId) {
            this.merchantId = merchantId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder category(TransactionCategory category) {
            this.category = category;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder transactionTimestamp(LocalDateTime transactionTimestamp) {
            this.transactionTimestamp = transactionTimestamp;
            return this;
        }

        public TransactionRequest build() {
            return new TransactionRequest(transactionId, customerId, merchantId, amount, currency,
                    category, description, transactionTimestamp);
        }
    }
}
