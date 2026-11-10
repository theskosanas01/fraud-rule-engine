package com.fraud.engine.dto;

import com.fraud.engine.entity.TransactionCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Response DTO for transaction with fraud analysis results")
public class TransactionResponse {

    @Schema(description = "Transaction database ID", example = "1")
    private Long id;

    @Schema(description = "Unique transaction identifier", example = "TXN-2024-001")
    private String transactionId;

    @Schema(description = "Customer identifier", example = "CUST-12345")
    private String customerId;

    @Schema(description = "Merchant identifier", example = "MERCH-67890")
    private String merchantId;

    @Schema(description = "Transaction amount", example = "150.50")
    private BigDecimal amount;

    @Schema(description = "ISO 4217 currency code", example = "USD")
    private String currency;

    @Schema(description = "Transaction category", example = "ONLINE_PURCHASE")
    private TransactionCategory category;

    @Schema(description = "Transaction description", example = "Electronics purchase")
    private String description;

    @Schema(description = "Whether transaction is flagged as fraud", example = "false")
    private Boolean isFlagged;

    @Schema(description = "Fraud risk score (0-100)", example = "45.50")
    private BigDecimal riskScore;

    @Schema(description = "List of triggered fraud rules", example = "[\"HIGH_AMOUNT_RULE\", \"UNUSUAL_TIME_RULE\"]")
    private List<String> fraudRulesTriggered;

    @Schema(description = "When the transaction occurred", example = "2024-01-15T10:30:00")
    private LocalDateTime transactionTimestamp;

    @Schema(description = "When the transaction was created in system", example = "2024-01-15T10:35:00")
    private LocalDateTime createdAt;

    @Schema(description = "When the transaction record was last updated", example = "2024-01-15T10:35:00")
    private LocalDateTime updatedAt;

    // Constructors
    public TransactionResponse() {
    }

    public TransactionResponse(Long id, String transactionId, String customerId, String merchantId,
                              BigDecimal amount, String currency, TransactionCategory category,
                              String description, Boolean isFlagged, BigDecimal riskScore,
                              List<String> fraudRulesTriggered, LocalDateTime transactionTimestamp,
                              LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.merchantId = merchantId;
        this.amount = amount;
        this.currency = currency;
        this.category = category;
        this.description = description;
        this.isFlagged = isFlagged;
        this.riskScore = riskScore;
        this.fraudRulesTriggered = fraudRulesTriggered;
        this.transactionTimestamp = transactionTimestamp;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Boolean getIsFlagged() {
        return isFlagged;
    }

    public void setIsFlagged(Boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public BigDecimal getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(BigDecimal riskScore) {
        this.riskScore = riskScore;
    }

    public List<String> getFraudRulesTriggered() {
        return fraudRulesTriggered;
    }

    public void setFraudRulesTriggered(List<String> fraudRulesTriggered) {
        this.fraudRulesTriggered = fraudRulesTriggered;
    }

    public LocalDateTime getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(LocalDateTime transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String transactionId;
        private String customerId;
        private String merchantId;
        private BigDecimal amount;
        private String currency;
        private TransactionCategory category;
        private String description;
        private Boolean isFlagged;
        private BigDecimal riskScore;
        private List<String> fraudRulesTriggered;
        private LocalDateTime transactionTimestamp;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

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

        public Builder isFlagged(Boolean isFlagged) {
            this.isFlagged = isFlagged;
            return this;
        }

        public Builder riskScore(BigDecimal riskScore) {
            this.riskScore = riskScore;
            return this;
        }

        public Builder fraudRulesTriggered(List<String> fraudRulesTriggered) {
            this.fraudRulesTriggered = fraudRulesTriggered;
            return this;
        }

        public Builder transactionTimestamp(LocalDateTime transactionTimestamp) {
            this.transactionTimestamp = transactionTimestamp;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public TransactionResponse build() {
            return new TransactionResponse(id, transactionId, customerId, merchantId, amount, currency,
                    category, description, isFlagged, riskScore, fraudRulesTriggered,
                    transactionTimestamp, createdAt, updatedAt);
        }
    }
}
