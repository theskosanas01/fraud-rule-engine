package com.fraud.engine.service;

import com.fraud.engine.entity.Transaction;
import com.fraud.engine.entity.FraudRule;
import com.fraud.engine.entity.TransactionCategory;
import com.fraud.engine.repository.FraudRuleRepository;
import com.fraud.engine.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FraudRuleEvaluatorTests {

    @InjectMocks
    private FraudRuleEvaluator fraudRuleEvaluator;

    @Mock
    private FraudRuleRepository fraudRuleRepository;

    @Mock
    private TransactionAnalysisService transactionAnalysisService;

    private Transaction transaction;
    private List<FraudRule> fraudRules;

    @BeforeEach
    void setUp() {
        transaction = Transaction.builder()
                .transactionId("TXN-2024-001")
                .customerId("CUST-12345")
                .merchantId("MERCH-67890")
                .amount(new BigDecimal("150.50"))
                .currency("USD")
                .category(TransactionCategory.ONLINE_PURCHASE)
                .transactionTimestamp(LocalDateTime.now())
                .isFlagged(false)
                .build();

        fraudRules = new ArrayList<>();
        
        // Mock TransactionAnalysisService methods
        when(transactionAnalysisService.getRecentTransactionsByCustomer(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        when(transactionAnalysisService.isMerchantKnown(any(), any()))
                .thenReturn(true);
    }

    @Test
    void testEvaluateTransaction_NoRulesTriggered() {
        when(fraudRuleRepository.findByIsActiveTrueAndCategoryOrderByPriorityAsc(
                TransactionCategory.ONLINE_PURCHASE))
                .thenReturn(fraudRules);

        FraudEvaluationResult result = fraudRuleEvaluator.evaluateTransaction(transaction);

        assertNotNull(result);
        assertFalse(result.getIsFlagged());
        assertEquals(BigDecimal.ZERO, result.getRiskScore());
        assertTrue(result.getTriggeredRules().isEmpty());
    }

    @Test
    void testEvaluateTransaction_HighAmountRuleTriggered() {
        transaction.setAmount(new BigDecimal("6000"));

        FraudRule highAmountRule = FraudRule.builder()
                .ruleName("HIGH_AMOUNT_RULE")
                .description("Flags transactions with amount exceeding 5000")
                .category(TransactionCategory.ONLINE_PURCHASE)
                .ruleExpression("amount > 5000")
                .riskScore(new BigDecimal("25.0"))
                .isActive(true)
                .priority(1)
                .build();

        fraudRules.add(highAmountRule);

        when(fraudRuleRepository.findByIsActiveTrueAndCategoryOrderByPriorityAsc(
                TransactionCategory.ONLINE_PURCHASE))
                .thenReturn(fraudRules);

        FraudEvaluationResult result = fraudRuleEvaluator.evaluateTransaction(transaction);

        assertNotNull(result);
        assertTrue(result.getIsFlagged());
        assertTrue(result.getRiskScore().compareTo(BigDecimal.ZERO) > 0);
        assertTrue(result.getTriggeredRules().contains("HIGH_AMOUNT_RULE"));
    }

    @Test
    void testEvaluateTransaction_CryptocurrencyRuleTriggered() {
        transaction.setCategory(TransactionCategory.CRYPTOCURRENCY);

        FraudRule cryptoRule = FraudRule.builder()
                .ruleName("CRYPTOCURRENCY_RULE")
                .description("Flags cryptocurrency transactions")
                .category(TransactionCategory.CRYPTOCURRENCY)
                .ruleExpression("category == CRYPTOCURRENCY")
                .riskScore(new BigDecimal("30.0"))
                .isActive(true)
                .priority(6)
                .build();

        fraudRules.add(cryptoRule);

        when(fraudRuleRepository.findByIsActiveTrueAndCategoryOrderByPriorityAsc(
                TransactionCategory.CRYPTOCURRENCY))
                .thenReturn(fraudRules);

        FraudEvaluationResult result = fraudRuleEvaluator.evaluateTransaction(transaction);

        assertNotNull(result);
        assertTrue(result.getIsFlagged());
        assertTrue(result.getTriggeredRules().contains("CRYPTOCURRENCY_RULE"));
    }
}
