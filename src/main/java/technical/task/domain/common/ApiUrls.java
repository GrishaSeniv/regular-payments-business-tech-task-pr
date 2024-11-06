package technical.task.domain.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
@Component
public class ApiUrls {

    @Value("${api.payment-instructions.url}")
    private String paymentInstructionBaseUrl;

    @Value("${api.transactions.url}")
    private String transactionBaseUrl;

    public String getPaymentInstructionBaseUrl() {
        return paymentInstructionBaseUrl;
    }

    public String getTransactionBaseUrl() {
        return transactionBaseUrl;
    }
}
