package technical.task.domain.model;

import technical.task.domain.model.payment_instruction.PaymentInstructionReq;
import technical.task.domain.model.payment_instruction.PaymentInstructionResp;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public class PaymentInstructionTestData {
    public static PaymentInstructionReq createPaymentInstructionReq() {
        return new PaymentInstructionReq(
                "John Doe Father",
                "1234567890",
                "1234567890123456",
                "UA1234567890123456789012345678901",
                "123456",
                "12345678",
                "Doe Enterprises",
                Duration.ofDays(1),
                new BigDecimal("1000.00")
        );
    }

    public static PaymentInstructionResp createPaymentInstructionResp() {
        return new PaymentInstructionResp(
                1L,
                "John Doe Father",
                "1234567890",
                "1234567890123456",
                "UA1234567890123456789012345678901",
                "123456",
                "12345678",
                "Doe Enterprises",
                Duration.ofDays(1),
                new BigDecimal("1000.00")
        );
    }
}