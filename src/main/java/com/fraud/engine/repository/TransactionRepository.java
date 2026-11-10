package com.fraud.engine.repository;

import com.fraud.engine.entity.Transaction;
import com.fraud.engine.entity.TransactionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Optional<Transaction> findByTransactionId(String transactionId);

    Page<Transaction> findByCustomerId(String customerId, Pageable pageable);

    Page<Transaction> findByIsFlaggedTrue(Pageable pageable);

    Page<Transaction> findByCategory(TransactionCategory category, Pageable pageable);

    List<Transaction> findByCustomerIdAndTransactionTimestampBetween(
            String customerId,
            LocalDateTime startTime,
            LocalDateTime endTime
    );

    Page<Transaction> findByIsFlaggedTrueAndCategory(
            TransactionCategory category,
            Pageable pageable
    );

    long countByIsFlaggedTrue();
}
