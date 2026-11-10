package com.fraud.engine.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fraud.engine.dto.TransactionRequest;
import com.fraud.engine.dto.TransactionResponse;
import com.fraud.engine.entity.TransactionCategory;
import com.fraud.engine.service.FraudRuleEvaluator;
import com.fraud.engine.service.TransactionAnalysisService;
import com.fraud.engine.service.TransactionMapper;
import com.fraud.engine.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TransactionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private FraudRuleEvaluator fraudRuleEvaluator;

    @MockBean
    private TransactionAnalysisService transactionAnalysisService;

    @MockBean
    private TransactionMapper transactionMapper;

    private TransactionRequest transactionRequest;
    private TransactionResponse transactionResponse;

    @BeforeEach
    void setUp() {
        transactionRequest = TransactionRequest.builder()
                .transactionId("TXN-2024-001")
                .customerId("CUST-12345")
                .merchantId("MERCH-67890")
                .amount(new BigDecimal("150.50"))
                .currency("ZAR")
                .category(TransactionCategory.ONLINE_PURCHASE)
                .description("Electronics purchase")
                .transactionTimestamp(LocalDateTime.now())
                .build();

        transactionResponse = TransactionResponse.builder()
                .id(1L)
                .transactionId("TXN-2024-001")
                .customerId("CUST-12345")
                .merchantId("MERCH-67890")
                .amount(new BigDecimal("150.50"))
                .currency("ZAR")
                .category(TransactionCategory.ONLINE_PURCHASE)
                .description("Electronics purchase")
                .isFlagged(false)
                .riskScore(new BigDecimal("0.00"))
                .fraudRulesTriggered(Collections.emptyList())
                .transactionTimestamp(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testProcessTransaction_Success() throws Exception {
        when(transactionService.processTransaction(any(TransactionRequest.class)))
                .thenReturn(transactionResponse);

        mockMvc.perform(post("/api/v1/transactions/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transactionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transactionId").value("TXN-2024-001"))
                .andExpect(jsonPath("$.isFlagged").value(false));
    }

    @Test
    void testProcessTransaction_InvalidRequest() throws Exception {
        TransactionRequest invalidRequest = TransactionRequest.builder()
                .transactionId("")  // Empty transaction ID
                .customerId("CUST-12345")
                .merchantId("MERCH-67890")
                .amount(new BigDecimal("150.50"))
                .currency("ZAR")
                .category(TransactionCategory.ONLINE_PURCHASE)
                .transactionTimestamp(LocalDateTime.now())
                .build();

        mockMvc.perform(post("/api/v1/transactions/process")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetTransactionById_Success() throws Exception {
        when(transactionService.getTransactionById(1L))
                .thenReturn(transactionResponse);

        mockMvc.perform(get("/api/v1/transactions/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.transactionId").value("TXN-2024-001"));
    }

    @Test
    void testGetFlaggedTransactions_Success() throws Exception {
        List<TransactionResponse> flaggedTransactions = new ArrayList<>();
        flaggedTransactions.add(transactionResponse);

        Page<TransactionResponse> page = new PageImpl<>(flaggedTransactions, 
                PageRequest.of(0, 20), 1);

        when(transactionService.getFlaggedTransactions(any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/transactions/flagged")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void testGetTransactionsByCustomerId_Success() throws Exception {
        List<TransactionResponse> customerTransactions = new ArrayList<>();
        customerTransactions.add(transactionResponse);

        Page<TransactionResponse> page = new PageImpl<>(customerTransactions, 
                PageRequest.of(0, 20), 1);

        when(transactionService.getTransactionsByCustomerId("CUST-12345", any()))
                .thenReturn(page);

        mockMvc.perform(get("/api/v1/transactions/customer/CUST-12345")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }
}
