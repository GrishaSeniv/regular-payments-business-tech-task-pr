package technical.task.domain.model;

import technical.task.domain.model.payment_instruction.PaymentInstructionResp;
import technical.task.domain.model.transaction.TransactionCreateReq;
import technical.task.domain.model.transaction.TransactionResp;
import technical.task.domain.model.transaction.TransactionUpdateReq;
import technical.task.domain.type.TransactionStatus;

import java.time.LocalDateTime;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-05
 */
public class TransactionTestData {
    public static TransactionCreateReq createTransactionCreateReq(PaymentInstructionResp respPayment1) {
        return new TransactionCreateReq(
                respPayment1.id(),
                respPayment1.paymentAmount(),
                TransactionStatus.A
        );
    }

    public static TransactionResp createTransactionResp1(PaymentInstructionResp respPayment1) {
        return new TransactionResp(
                1L,
                respPayment1,
                LocalDateTime.now(),
                respPayment1.paymentAmount(),
                TransactionStatus.A
        );
    }

    public static TransactionResp updatedStornedTransactionResp(TransactionResp createdResp) {
        return new TransactionResp(
                createdResp.id(),
                createdResp.paymentInstruction(),
                createdResp.transactionDate(),
                createdResp.transactionAmount(),
                TransactionStatus.S
        );
    }

    public static TransactionUpdateReq createTransactionUpdateReq() {
        return new TransactionUpdateReq(TransactionStatus.S);
    }
}
