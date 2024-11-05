package technical.task.domain.model.payment_instruction;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public record PaymentInstructionResp(Long id, String payerName, String payerInn, String payerCardNumber,
                                     String recipientAccountNumber,
                                     String recipientMfo, String recipientOkpo, String recipientName,
                                     Duration periodInterval,
                                     BigDecimal paymentAmount) {
}
