package technical.task.payment_processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import technical.task.domain.PaymentProcessingAware;
import technical.task.domain.client.PaymentInstructionClient;
import technical.task.domain.client.TransactionClient;
import technical.task.domain.common.Preconditions;
import technical.task.domain.exception.DebitPaymentBadRequestException;
import technical.task.domain.model.payment_instruction.PaymentInstructionReq;
import technical.task.domain.model.payment_instruction.PaymentInstructionResp;
import technical.task.domain.model.payment_instruction.PaymentInstructionSearch;
import technical.task.domain.model.transaction.TransactionResp;
import technical.task.domain.model.transaction.TransactionSearch;

import java.time.LocalDateTime;
import java.util.List;

import static technical.task.payment_processing.Converter.toTransactionCreateReq;
import static technical.task.payment_processing.Converter.toTransactionUpdateReq;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
@Service
class PaymentProcessingService implements PaymentProcessingAware {
    private final Logger logger = LoggerFactory.getLogger(PaymentProcessingService.class);
    private final PaymentInstructionClient paymentInstructionClient;
    private final TransactionClient transactionClient;

    PaymentProcessingService(PaymentInstructionClient paymentInstructionClient,
                             TransactionClient transactionClient) {
        this.paymentInstructionClient = paymentInstructionClient;
        this.transactionClient = transactionClient;
    }

    @Override
    public void processDuePayments() {
        int batchSize = 1000;
        int offset = 0;
        boolean hasMorePayments = true;

        while (hasMorePayments) {
            List<PaymentInstructionResp> paymentsBatch = searchPayments(
                    (PaymentInstructionSearch) new PaymentInstructionSearch().setLimit(batchSize).setOffset(offset));
            if (paymentsBatch.isEmpty()) {
                hasMorePayments = false;
            } else {
                paymentsBatch.parallelStream().forEach(payment -> {
                    try {
                        processDebit(payment);
                    } catch (DebitPaymentBadRequestException ignore) {
                    }
                });
                offset += batchSize;
            }
        }
    }

    public PaymentInstructionResp createPayment(PaymentInstructionReq req) {
        validatePaymentReq(req);
        logger.info("Create payment request");
        return paymentInstructionClient.create(req);
    }

    public TransactionResp debitPayment(Long paymentId) {
        PaymentInstructionResp payment = paymentInstructionClient.getById(paymentId);
        return processDebit(payment);
    }

    public TransactionResp markTransactionAsStorned(Long transactionId) {
        logger.info("Set transaction status to storned for: {}", transactionId);
        return transactionClient.update(toTransactionUpdateReq(), transactionId);
    }

    public List<PaymentInstructionResp> searchPayments(PaymentInstructionSearch req) {
        logger.info("Search payments by req: {}", req);
        return paymentInstructionClient.search(req);
    }

    public List<TransactionResp> searchTransactions(TransactionSearch req) {
        logger.info("Search transactions by req: {}", req);
        return transactionClient.search(req);
    }

    /**
     * Field validations such as non-blank constraints and size restrictions are handled
     * by Jakarta Bean Validation annotations, ensuring that the inputs meet the required
     * criteria before processing.
     */
    private void validatePaymentReq(PaymentInstructionReq req) {
        Preconditions.checkFullNameStr(req.payerName());
    }

    private TransactionResp processDebit(PaymentInstructionResp payment) {
        Long paymentId = payment.id();
        logger.info("Debit payment started for paymentId: {}", paymentId);
        TransactionResp lastTransaction = getLastTransaction(paymentId);
        if (lastTransaction == null) {
            logger.info("Creating first transaction for paymentId: {}", paymentId);
            return transactionClient.create(toTransactionCreateReq(payment));
        }
        LocalDateTime lastTransactionDate = lastTransaction.transactionDate();
        LocalDateTime nextDueDate = lastTransactionDate.plus(payment.periodInterval());
        if (LocalDateTime.now().isAfter(nextDueDate)) {
            logger.info("Creating next transaction for paymentId: {}", paymentId);
            return transactionClient.create(toTransactionCreateReq(payment));
        }
        String msg = "Payment is not yet due for debit.";
        logger.error(msg);
        throw new DebitPaymentBadRequestException(msg);
    }

    private TransactionResp getLastTransaction(Long paymentId) {
        List<TransactionResp> list = transactionClient.search(getLastTransactionSearch(paymentId));
        if (list.isEmpty()) {
            return null;
        }
        return list.getFirst();
    }

    // The first element will be our last transaction
    private TransactionSearch getLastTransactionSearch(Long paymentId) {
        return (TransactionSearch) new TransactionSearch(paymentId)
                .setLimit(1);
    }
}
