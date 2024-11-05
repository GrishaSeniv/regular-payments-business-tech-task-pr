package technical.task.payment_processing;

import technical.task.domain.model.payment_instruction.PaymentInstructionResp;
import technical.task.domain.model.transaction.TransactionCreateReq;
import technical.task.domain.model.transaction.TransactionUpdateReq;
import technical.task.domain.type.TransactionStatus;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-05
 */
class Converter {
    static TransactionCreateReq toTransactionCreateReq(PaymentInstructionResp paymentInstructionResp) {
        return new TransactionCreateReq(paymentInstructionResp.id(), paymentInstructionResp.paymentAmount(), TransactionStatus.A);
    }

    static TransactionUpdateReq toTransactionUpdateReq() {
        return new TransactionUpdateReq(TransactionStatus.S);
    }
}
