package technical.task.domain.model.payment_instruction;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public record PaymentInstructionReq(@NotBlank @Size(max = 255) String payerName,
                                    @NotBlank @Size(max = 10) String payerInn,
                                    @NotBlank @Size(max = 20) String payerCardNumber,
                                    @NotBlank @Size(max = 34) String recipientAccountNumber,
                                    @NotBlank @Size(max = 6) String recipientMfo,
                                    @NotBlank @Size(max = 8) String recipientOkpo,
                                    @NotBlank @Size(max = 255) String recipientName,
                                    @NotNull Duration periodInterval,
                                    @NotNull BigDecimal paymentAmount) {
}
