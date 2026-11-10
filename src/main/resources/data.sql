-- Insert default fraud rules
INSERT INTO fraud_rules (rule_name, description, category, rule_expression, risk_score, is_active, priority, created_at, updated_at)
VALUES 
('HIGH_AMOUNT_RULE', 'Flags transactions with amount exceeding 5000 ZAR', 'ONLINE_PURCHASE', 'amount > 5000', 25.0, true, 1, NOW(), NOW()),
('UNUSUAL_TIME_RULE', 'Flags transactions made between 2 AM and 5 AM', 'ONLINE_PURCHASE', 'hour(timestamp) BETWEEN 2 AND 5', 15.0, true, 2, NOW(), NOW()),
('MULTIPLE_TRANSACTIONS_RULE', 'Flags multiple transactions within 1 hour from same customer', 'ONLINE_PURCHASE', 'count(txn_per_hour) > 5', 20.0, true, 3, NOW(), NOW()),
('INTERNATIONAL_TRANSACTION_RULE', 'Flags international transactions', 'INTERNATIONAL', 'category == INTERNATIONAL', 10.0, true, 4, NOW(), NOW()),
('UNUSUAL_MERCHANT_RULE', 'Flags transactions with merchants not previously used', 'ONLINE_PURCHASE', 'merchant NOT IN (previous_merchants)', 18.0, true, 5, NOW(), NOW()),
('CRYPTOCURRENCY_RULE', 'Flags cryptocurrency transactions', 'CRYPTOCURRENCY', 'category == CRYPTOCURRENCY', 30.0, true, 6, NOW(), NOW());
