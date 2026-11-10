package com.fraud.engine.repository;

import com.fraud.engine.entity.FraudRule;
import com.fraud.engine.entity.TransactionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FraudRuleRepository extends JpaRepository<FraudRule, Long> {

    Optional<FraudRule> findByRuleName(String ruleName);

    List<FraudRule> findByIsActiveTrueOrderByPriorityAsc();

    List<FraudRule> findByIsActiveTrueAndCategoryOrderByPriorityAsc(TransactionCategory category);
}
