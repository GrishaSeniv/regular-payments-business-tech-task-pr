package technical.task.domain.model.transaction;

import jakarta.validation.constraints.NotNull;
import technical.task.domain.type.TransactionStatus;

import java.math.BigDecimal;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public record TransactionCreateReq(Long paymentInstructionId,
                                   @NotNull BigDecimal transactionAmount,
                                   @NotNull TransactionStatus status) {
}
