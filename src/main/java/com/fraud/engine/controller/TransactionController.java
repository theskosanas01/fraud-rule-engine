package com.fraud.engine.controller;

import com.fraud.engine.dto.TransactionRequest;
import com.fraud.engine.dto.TransactionResponse;
import com.fraud.engine.entity.TransactionCategory;
import com.fraud.engine.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/transactions")
@Tag(name = "Transaction Management", description = "APIs for transaction processing and fraud detection")
public class TransactionController {

    private static final Logger log = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/process")
    @Operation(summary = "Process a new transaction", description = "Submits a transaction for fraud analysis and stores the result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transaction processed successfully",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid transaction request"),
            @ApiResponse(responseCode = "409", description = "Transaction ID already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TransactionResponse> processTransaction(
            @Valid @RequestBody TransactionRequest request) {
        log.info("Received transaction request: {}", request.getTransactionId());
        TransactionResponse response = transactionService.processTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieves a specific transaction by its database ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found",
                    content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TransactionResponse> getTransactionById(
            @Parameter(description = "Transaction database ID", example = "1")
            @PathVariable Long id) {
        log.info("Fetching transaction with ID: {}", id);
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @GetMapping("/transaction-id/{transactionId}")
    @Operation(summary = "Get transaction by transaction ID", description = "Retrieves a transaction by its unique transaction identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transaction found"),
            @ApiResponse(responseCode = "404", description = "Transaction not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<TransactionResponse> getTransactionByTransactionId(
            @Parameter(description = "Unique transaction identifier", example = "TXN-2024-001")
            @PathVariable String transactionId) {
        log.info("Fetching transaction with transaction ID: {}", transactionId);
        return ResponseEntity.ok(transactionService.getTransactionByTransactionId(transactionId));
    }

    @GetMapping("/flagged")
    @Operation(summary = "Get flagged transactions", description = "Retrieves all transactions flagged as potential fraud")
    @ApiResponse(responseCode = "200", description = "List of flagged transactions retrieved successfully")
    public ResponseEntity<Page<TransactionResponse>> getFlaggedTransactions(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        log.info("Fetching flagged transactions: page={}, size={}", page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return ResponseEntity.ok(transactionService.getFlaggedTransactions(pageable));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get transactions by customer", description = "Retrieves all transactions for a specific customer")
    @ApiResponse(responseCode = "200", description = "Customer transactions retrieved successfully")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByCustomerId(
            @Parameter(description = "Customer identifier", example = "CUST-12345")
            @PathVariable String customerId,
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        log.info("Fetching transactions for customer: {} (page={}, size={})", customerId, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return ResponseEntity.ok(transactionService.getTransactionsByCustomerId(customerId, pageable));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get transactions by category", description = "Retrieves transactions filtered by transaction category")
    @ApiResponse(responseCode = "200", description = "Category transactions retrieved successfully")
    public ResponseEntity<Page<TransactionResponse>> getTransactionsByCategory(
            @Parameter(description = "Transaction category", example = "ONLINE_PURCHASE")
            @PathVariable TransactionCategory category,
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        log.info("Fetching transactions for category: {} (page={}, size={})", category, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return ResponseEntity.ok(transactionService.getTransactionsByCategory(category, pageable));
    }

    @GetMapping("/flagged/{category}")
    @Operation(summary = "Get flagged transactions by category", description = "Retrieves flagged transactions filtered by category")
    @ApiResponse(responseCode = "200", description = "Flagged category transactions retrieved successfully")
    public ResponseEntity<Page<TransactionResponse>> getFlaggedTransactionsByCategory(
            @Parameter(description = "Transaction category", example = "ONLINE_PURCHASE")
            @PathVariable TransactionCategory category,
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "createdAt") String sort,
            @Parameter(description = "Sort direction")
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {
        log.info("Fetching flagged transactions for category: {} (page={}, size={})", category, page, size);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        return ResponseEntity.ok(transactionService.getFlaggedTransactionsByCategory(category, pageable));
    }
}
