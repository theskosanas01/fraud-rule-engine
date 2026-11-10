package com.fraud.engine.service;

import com.fraud.engine.entity.Transaction;
import com.fraud.engine.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TransactionAnalysisService {

    private static final Logger log = LoggerFactory.getLogger(TransactionAnalysisService.class);
    private final TransactionRepository transactionRepository;

    public TransactionAnalysisService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    /**
     * Retrieves recent transactions for a customer within a time range
     */
    public List<Transaction> getRecentTransactionsByCustomer(
            String customerId,
            LocalDateTime startTime,
            LocalDateTime endTime) {
        return transactionRepository.findByCustomerIdAndTransactionTimestampBetween(
                customerId,
                startTime,
                endTime
        );
    }

    /**
     * Checks if a merchant is known (previously used) by a customer
     */
    public boolean isMerchantKnown(String customerId, String merchantId) {
        // Get all previous transactions by this customer
        List<Transaction> allTransactions = transactionRepository.findByCustomerId(customerId, 
                org.springframework.data.domain.PageRequest.of(0, 1000)).getContent();

        return allTransactions.stream()
                .anyMatch(t -> t.getMerchantId().equals(merchantId));
    }

    /**
     * Gets unique merchants used by a customer
     */
    public Set<String> getCustomerMerchants(String customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId,
                org.springframework.data.domain.PageRequest.of(0, 1000)).getContent();

        Set<String> merchants = new HashSet<>();
        transactions.forEach(t -> merchants.add(t.getMerchantId()));
        return merchants;
    }

    /**
     * Gets customer transaction frequency
     */
    public long getTransactionFrequency(String customerId, LocalDateTime startTime, LocalDateTime endTime) {
        return getRecentTransactionsByCustomer(customerId, startTime, endTime).size();
    }
}
