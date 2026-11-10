package com.fraud.engine.service;

import java.math.BigDecimal;
import java.util.List;

public class FraudEvaluationResult {
    private Boolean isFlagged;
    private BigDecimal riskScore;
    private List<String> triggeredRules;

    public FraudEvaluationResult() {
    }

    public FraudEvaluationResult(Boolean isFlagged, BigDecimal riskScore, List<String> triggeredRules) {
        this.isFlagged = isFlagged;
        this.riskScore = riskScore;
        this.triggeredRules = triggeredRules;
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

    public List<String> getTriggeredRules() {
        return triggeredRules;
    }

    public void setTriggeredRules(List<String> triggeredRules) {
        this.triggeredRules = triggeredRules;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Boolean isFlagged;
        private BigDecimal riskScore;
        private List<String> triggeredRules;

        public Builder isFlagged(Boolean isFlagged) {
            this.isFlagged = isFlagged;
            return this;
        }

        public Builder riskScore(BigDecimal riskScore) {
            this.riskScore = riskScore;
            return this;
        }

        public Builder triggeredRules(List<String> triggeredRules) {
            this.triggeredRules = triggeredRules;
            return this;
        }

        public FraudEvaluationResult build() {
            return new FraudEvaluationResult(isFlagged, riskScore, triggeredRules);
        }
    }
}
