package technical.task.payment_processing;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import technical.task.domain.model.payment_instruction.PaymentInstructionReq;
import technical.task.domain.model.payment_instruction.PaymentInstructionResp;
import technical.task.domain.model.payment_instruction.PaymentInstructionSearch;
import technical.task.domain.model.transaction.TransactionResp;
import technical.task.domain.model.transaction.TransactionSearch;

import java.util.List;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
@RestController
@RequestMapping("/api/v1/business/payments")
class PaymentProcessingController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentProcessingController.class);
    private final PaymentProcessingService service;

    PaymentProcessingController(PaymentProcessingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentInstructionResp> create(@RequestBody @Valid PaymentInstructionReq req) {
        PaymentInstructionResp resp = service.createPayment(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PostMapping("{id}")
    public ResponseEntity<TransactionResp> debitPayment(@PathVariable Long id) {
        TransactionResp resp = service.debitPayment(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(resp);
    }

    @PatchMapping("/transactions/{id}")
    public ResponseEntity<TransactionResp> markTransactionAsStorned(@PathVariable Long id) {
        TransactionResp resp = service.markTransactionAsStorned(id);
        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @GetMapping
    public ResponseEntity<List<PaymentInstructionResp>> searchPayments(PaymentInstructionSearch req) {
        List<PaymentInstructionResp> resp = service.searchPayments(req);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResp>> searchTransactions(TransactionSearch req) {
        List<TransactionResp> resp = service.searchTransactions(req);
        return ResponseEntity.ok(resp);
    }
}
