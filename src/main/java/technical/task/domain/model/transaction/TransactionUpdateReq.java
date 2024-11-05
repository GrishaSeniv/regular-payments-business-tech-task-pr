package technical.task.domain.model.transaction;

import jakarta.validation.constraints.NotNull;
import technical.task.domain.type.TransactionStatus;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public record TransactionUpdateReq(@NotNull TransactionStatus status) {
}
