package technical.task.domain.model.transaction;

import technical.task.domain.model.payment_instruction.PaymentInstructionResp;
import technical.task.domain.type.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public record TransactionResp(Long id, PaymentInstructionResp paymentInstruction, LocalDateTime transactionDate,
                              BigDecimal transactionAmount, TransactionStatus status) {
}
