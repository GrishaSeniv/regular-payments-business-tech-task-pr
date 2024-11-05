package technical.task.domain.model.payment_instruction;

import technical.task.domain.SearchAware;
import technical.task.domain.model.Search;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public class PaymentInstructionSearch extends Search implements SearchAware {
    private String payerInn;
    private String recipientOkpo;

    public String getPayerInn() {
        return payerInn;
    }

    public PaymentInstructionSearch setPayerInn(String payerInn) {
        this.payerInn = payerInn;
        return this;
    }

    public String getRecipientOkpo() {
        return recipientOkpo;
    }

    public PaymentInstructionSearch setRecipientOkpo(String recipientOkpo) {
        this.recipientOkpo = recipientOkpo;
        return this;
    }

    @Override
    public String toString() {
        return "PaymentInstructionSearch{" +
                "payerInn='" + payerInn + '\'' +
                ", recipientOkpo='" + recipientOkpo + '\'' +
                "} " + super.toString();
    }
}
