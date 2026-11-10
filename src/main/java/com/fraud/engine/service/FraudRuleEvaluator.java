package com.fraud.engine.service;

import com.fraud.engine.entity.Transaction;
import com.fraud.engine.entity.FraudRule;
import com.fraud.engine.entity.TransactionCategory;
import com.fraud.engine.repository.FraudRuleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class FraudRuleEvaluator {

    private static final Logger log = LoggerFactory.getLogger(FraudRuleEvaluator.class);
    private final FraudRuleRepository fraudRuleRepository;
    private final TransactionAnalysisService transactionAnalysisService;

    public FraudRuleEvaluator(FraudRuleRepository fraudRuleRepository, TransactionAnalysisService transactionAnalysisService) {
        this.fraudRuleRepository = fraudRuleRepository;
        this.transactionAnalysisService = transactionAnalysisService;
    }

    /**
     * Evaluates all active fraud rules for a given transaction
     */
    public FraudEvaluationResult evaluateTransaction(Transaction transaction) {
        log.info("FRAUD-EVAL: Starting evaluation for transaction: {}", transaction.getTransactionId());
        log.info("FRAUD-EVAL: Transaction details - Amount: {}, Category: {}, Customer: {}", 
                transaction.getAmount(), transaction.getCategory(), transaction.getCustomerId());

        List<FraudRule> applicableRules = fraudRuleRepository
                .findByIsActiveTrueAndCategoryOrderByPriorityAsc(transaction.getCategory());

        log.info("FRAUD-EVAL: Found {} applicable rules for category {}", applicableRules.size(), transaction.getCategory());

        List<String> triggeredRules = new ArrayList<>();
        BigDecimal totalRiskScore = BigDecimal.ZERO;
        boolean isFlaggedForFraud = false;

        for (FraudRule rule : applicableRules) {
            boolean triggered = evaluateRule(transaction, rule);
            log.info("FRAUD-EVAL: Rule {} triggered: {}", rule.getRuleName(), triggered);
            
            if (triggered) {
                triggeredRules.add(rule.getRuleName());
                totalRiskScore = totalRiskScore.add(rule.getRiskScore());
                log.debug("Rule '{}' triggered for transaction {}", rule.getRuleName(), transaction.getTransactionId());
            }
        }

        log.info("FRAUD-EVAL: Evaluation complete - Rules triggered: {}, Total risk score: {}", 
                triggeredRules.size(), totalRiskScore);

        // Cap risk score at 100
        if (totalRiskScore.compareTo(new BigDecimal("100")) > 0) {
            totalRiskScore = new BigDecimal("100");
        }

        // Flag if any rules triggered and risk score is above threshold
        isFlaggedForFraud = !triggeredRules.isEmpty() && totalRiskScore.compareTo(new BigDecimal("50")) >= 0;
        
        log.info("FRAUD-EVAL: Final decision - Flagged: {}, Final risk score: {}", 
                isFlaggedForFraud, totalRiskScore);

        return FraudEvaluationResult.builder()
                .isFlagged(isFlaggedForFraud)
                .riskScore(totalRiskScore)
                .triggeredRules(triggeredRules)
                .build();
    }

    /**
     * Evaluates a specific rule against a transaction
     */
    private boolean evaluateRule(Transaction transaction, FraudRule rule) {
        switch (rule.getRuleName()) {
            case "HIGH_AMOUNT_RULE":
                return evaluateHighAmountRule(transaction);
            case "UNUSUAL_TIME_RULE":
                return evaluateUnusualTimeRule(transaction);
            case "MULTIPLE_TRANSACTIONS_RULE":
                return evaluateMultipleTransactionsRule(transaction);
            case "INTERNATIONAL_TRANSACTION_RULE":
                return evaluateInternationalTransactionRule(transaction);
            case "UNUSUAL_MERCHANT_RULE":
                return evaluateUnusualMerchantRule(transaction);
            case "CRYPTOCURRENCY_RULE":
                return evaluateCryptocurrencyRule(transaction);
            default:
                log.warn("Unknown rule: {}", rule.getRuleName());
                return false;
        }
    }

    /**
     * HIGH_AMOUNT_RULE: Flags transactions above a threshold amount
     */
    private boolean evaluateHighAmountRule(Transaction transaction) {
        BigDecimal threshold = new BigDecimal("5000");
        boolean triggered = transaction.getAmount().compareTo(threshold) > 0;
        log.info("FRAUD-RULE: HIGH_AMOUNT_RULE - Amount: {}, Threshold: {}, Triggered: {}", 
                transaction.getAmount(), threshold, triggered);
        return triggered;
    }

    /**
     * UNUSUAL_TIME_RULE: Flags transactions at unusual times (between 2 AM and 5 AM)
     */
    private boolean evaluateUnusualTimeRule(Transaction transaction) {
        int hour = transaction.getTransactionTimestamp().getHour();
        boolean triggered = hour >= 2 && hour <= 5;
        log.info("FRAUD-RULE: UNUSUAL_TIME_RULE - Hour: {}, Triggered: {}", hour, triggered);
        return triggered;
    }

    /**
     * MULTIPLE_TRANSACTIONS_RULE: Flags multiple transactions from same customer within short time
     */
    private boolean evaluateMultipleTransactionsRule(Transaction transaction) {
        LocalDateTime oneHourAgo = transaction.getTransactionTimestamp().minusHours(1);
        LocalDateTime now = transaction.getTransactionTimestamp();

        List<Transaction> recentTransactions = transactionAnalysisService
                .getRecentTransactionsByCustomer(transaction.getCustomerId(), oneHourAgo, now);

        boolean triggered = recentTransactions.size() > 5;
        log.info("FRAUD-RULE: MULTIPLE_TRANSACTIONS_RULE - Customer: {}, Recent count: {}, Threshold: 5, Triggered: {}", 
                transaction.getCustomerId(), recentTransactions.size(), triggered);
        
        return triggered;
    }

    /**
     * INTERNATIONAL_TRANSACTION_RULE: Flags international transactions
     */
    private boolean evaluateInternationalTransactionRule(Transaction transaction) {
        boolean triggered = transaction.getCategory() == TransactionCategory.INTERNATIONAL;
        log.info("FRAUD-RULE: INTERNATIONAL_TRANSACTION_RULE - Category: {}, Triggered: {}", 
                transaction.getCategory(), triggered);
        return triggered;
    }

    /**
     * UNUSUAL_MERCHANT_RULE: Flags transactions with merchants not previously used
     */
    private boolean evaluateUnusualMerchantRule(Transaction transaction) {
        boolean isKnown = transactionAnalysisService.isMerchantKnown(
                transaction.getCustomerId(),
                transaction.getMerchantId()
        );
        boolean triggered = !isKnown;
        log.info("FRAUD-RULE: UNUSUAL_MERCHANT_RULE - Customer: {}, Merchant: {}, Known: {}, Triggered: {}", 
                transaction.getCustomerId(), transaction.getMerchantId(), isKnown, triggered);
        return triggered;
    }

    /**
     * CRYPTOCURRENCY_RULE: Flags cryptocurrency transactions
     */
    private boolean evaluateCryptocurrencyRule(Transaction transaction) {
        boolean triggered = transaction.getCategory() == TransactionCategory.CRYPTOCURRENCY;
        log.info("FRAUD-RULE: CRYPTOCURRENCY_RULE - Category: {}, Triggered: {}", 
                transaction.getCategory(), triggered);
        return triggered;
    }
}
