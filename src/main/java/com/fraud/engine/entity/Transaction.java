package com.fraud.engine.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_id", nullable = false, unique = true, length = 50)
    private String transactionId;

    @Column(name = "customer_id", nullable = false, length = 50)
    private String customerId;

    @Column(name = "merchant_id", nullable = false, length = 50)
    private String merchantId;

    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    private TransactionCategory category;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "is_flagged", nullable = false)
    private Boolean isFlagged;

    @Column(name = "risk_score", precision = 5, scale = 2)
    private BigDecimal riskScore;

    @Column(name = "fraud_rules_triggered", length = 1000)
    private String fraudRulesTriggered;

    @Column(name = "transaction_timestamp", nullable = false)
    private LocalDateTime transactionTimestamp;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Constructors
    public Transaction() {
    }

    public Transaction(Long id, String transactionId, String customerId, String merchantId,
                      BigDecimal amount, String currency, TransactionCategory category,
                      String description, Boolean isFlagged, BigDecimal riskScore,
                      String fraudRulesTriggered, LocalDateTime transactionTimestamp,
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

    public String getFraudRulesTriggered() {
        return fraudRulesTriggered;
    }

    public void setFraudRulesTriggered(String fraudRulesTriggered) {
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

    // Builder pattern
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
        private String fraudRulesTriggered;
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

        public Builder fraudRulesTriggered(String fraudRulesTriggered) {
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

        public Transaction build() {
            return new Transaction(id, transactionId, customerId, merchantId, amount, currency,
                    category, description, isFlagged, riskScore, fraudRulesTriggered,
                    transactionTimestamp, createdAt, updatedAt);
        }
    }
}
