package technical.task.domain.model.transaction;

import technical.task.domain.SearchAware;
import technical.task.domain.model.Search;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public class TransactionSearch extends Search implements SearchAware {
    private Long paymentInstructionId;

    public TransactionSearch() {
    }

    public TransactionSearch(Long paymentInstructionId) {
        this.paymentInstructionId = paymentInstructionId;
    }

    public Long getPaymentInstructionId() {
        return paymentInstructionId;
    }

    public TransactionSearch setPaymentInstructionId(Long paymentInstructionId) {
        this.paymentInstructionId = paymentInstructionId;
        return this;
    }

    @Override
    public String toString() {
        return "TransactionSearch{" +
                "paymentInstructionId=" + paymentInstructionId +
                "} " + super.toString();
    }
}
