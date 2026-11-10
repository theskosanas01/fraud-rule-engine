package com.fraud.engine.controller;

import com.fraud.engine.repository.TransactionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/stats")
@Tag(name = "Statistics", description = "Fraud detection statistics and system metrics")
public class ActuatorController {

    private static final Logger log = LoggerFactory.getLogger(ActuatorController.class);
    private final TransactionRepository transactionRepository;

    public ActuatorController(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/fraud-statistics")
    @Operation(summary = "Get fraud statistics", description = "Retrieves fraud detection statistics including total transactions and flagged count")
    public ResponseEntity<Map<String, Object>> getFraudStatistics() {
        log.info("Fetching fraud statistics");

        long totalTransactions = transactionRepository.count();
        long flaggedTransactions = transactionRepository.countByIsFlaggedTrue();
        
        double fraudPercentage = totalTransactions > 0 
                ? (flaggedTransactions * 100.0) / totalTransactions 
                : 0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("total_transactions", totalTransactions);
        stats.put("flagged_transactions", flaggedTransactions);
        stats.put("clean_transactions", totalTransactions - flaggedTransactions);
        stats.put("fraud_percentage", String.format("%.2f", fraudPercentage));

        return ResponseEntity.ok(stats);
    }
}
