package com.fraud.engine.service;

import com.fraud.engine.dto.TransactionRequest;
import com.fraud.engine.dto.TransactionResponse;
import com.fraud.engine.entity.Transaction;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class TransactionMapper {

    public Transaction toEntity(TransactionRequest request) {
        return Transaction.builder()
                .transactionId(request.getTransactionId())
                .customerId(request.getCustomerId())
                .merchantId(request.getMerchantId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .category(request.getCategory())
                .description(request.getDescription())
                .transactionTimestamp(request.getTransactionTimestamp())
                .isFlagged(false)
                .build();
    }

    public TransactionResponse toResponse(Transaction transaction) {
        List<String> fraudRules = transaction.getFraudRulesTriggered() != null
                && !transaction.getFraudRulesTriggered().isEmpty()
                ? Arrays.asList(transaction.getFraudRulesTriggered().split(","))
                : Collections.emptyList();

        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionId(transaction.getTransactionId())
                .customerId(transaction.getCustomerId())
                .merchantId(transaction.getMerchantId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .category(transaction.getCategory())
                .description(transaction.getDescription())
                .isFlagged(transaction.getIsFlagged())
                .riskScore(transaction.getRiskScore())
                .fraudRulesTriggered(fraudRules)
                .transactionTimestamp(transaction.getTransactionTimestamp())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }
}
