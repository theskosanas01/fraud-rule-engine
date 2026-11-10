package com.fraud.engine.service;

import com.fraud.engine.dto.TransactionRequest;
import com.fraud.engine.dto.TransactionResponse;
import com.fraud.engine.entity.Transaction;
import com.fraud.engine.entity.TransactionCategory;
import com.fraud.engine.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    private final TransactionRepository transactionRepository;
    private final FraudRuleEvaluator fraudRuleEvaluator;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, 
                             FraudRuleEvaluator fraudRuleEvaluator,
                             TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.fraudRuleEvaluator = fraudRuleEvaluator;
        this.transactionMapper = transactionMapper;
    }

    /**
     * Processes a new transaction and applies fraud rules
     */
    @Transactional
    public TransactionResponse processTransaction(TransactionRequest request) {
        log.info("SERVICE: Processing new transaction - ID: {}, Customer: {}, Amount: {} {}, Category: {}", 
                request.getTransactionId(), 
                request.getCustomerId(), 
                request.getAmount(), 
                request.getCurrency(),
                request.getCategory());

        // Check if transaction already exists
        if (transactionRepository.findByTransactionId(request.getTransactionId()).isPresent()) {
            log.warn("SERVICE: Duplicate transaction attempt - {}", request.getTransactionId());
            throw new IllegalArgumentException("Transaction with ID " + request.getTransactionId() + " already exists");
        }

        // Create transaction entity
        Transaction transaction = transactionMapper.toEntity(request);
        log.debug("SERVICE: Created transaction entity for {}", request.getTransactionId());

        // Evaluate fraud rules
        log.info("SERVICE: Starting fraud evaluation for transaction {}", request.getTransactionId());
        FraudEvaluationResult evaluationResult = fraudRuleEvaluator.evaluateTransaction(transaction);
        
        // Update transaction with evaluation results
        transaction.setIsFlagged(evaluationResult.getIsFlagged());
        transaction.setRiskScore(evaluationResult.getRiskScore());
        transaction.setFraudRulesTriggered(String.join(",", evaluationResult.getTriggeredRules()));

        log.info("SERVICE: Fraud evaluation complete - Rules triggered: {}, Risk Score: {}, Flagged: {}", 
                evaluationResult.getTriggeredRules().size(),
                evaluationResult.getRiskScore(),
                evaluationResult.getIsFlagged());

        // Save to database
        Transaction savedTransaction = transactionRepository.save(transaction);
        log.info("SERVICE: Transaction {} saved with ID {}", request.getTransactionId(), savedTransaction.getId());

        TransactionResponse response = transactionMapper.toResponse(savedTransaction);
        log.info("SERVICE: Transaction {} processing complete. Final status - Flagged: {}, Risk Score: {}", 
                request.getTransactionId(),
                response.getIsFlagged(),
                response.getRiskScore());

        return response;
    }

    /**
     * Retrieves a transaction by ID
     */
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long id) {
        return transactionRepository.findById(id)
                .map(transactionMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + id));
    }

    /**
     * Retrieves a transaction by transaction ID
     */
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByTransactionId(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId)
                .map(transactionMapper::toResponse)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found with ID: " + transactionId));
    }

    /**
     * Retrieves all flagged transactions
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getFlaggedTransactions(Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByIsFlaggedTrue(pageable);
        return new PageImpl<>(
                transactions.stream()
                        .map(transactionMapper::toResponse)
                        .collect(Collectors.toList()),
                pageable,
                transactions.getTotalElements()
        );
    }

    /**
     * Retrieves transactions by customer ID
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactionsByCustomerId(String customerId, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByCustomerId(customerId, pageable);
        return new PageImpl<>(
                transactions.stream()
                        .map(transactionMapper::toResponse)
                        .collect(Collectors.toList()),
                pageable,
                transactions.getTotalElements()
        );
    }

    /**
     * Retrieves transactions by category
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactionsByCategory(TransactionCategory category, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByCategory(category, pageable);
        return new PageImpl<>(
                transactions.stream()
                        .map(transactionMapper::toResponse)
                        .collect(Collectors.toList()),
                pageable,
                transactions.getTotalElements()
        );
    }

    /**
     * Retrieves flagged transactions by category
     */
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getFlaggedTransactionsByCategory(TransactionCategory category, Pageable pageable) {
        Page<Transaction> transactions = transactionRepository.findByIsFlaggedTrueAndCategory(category, pageable);
        return new PageImpl<>(
                transactions.stream()
                        .map(transactionMapper::toResponse)
                        .collect(Collectors.toList()),
                pageable,
                transactions.getTotalElements()
        );
    }
}
