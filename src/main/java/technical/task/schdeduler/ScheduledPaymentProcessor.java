package technical.task.schdeduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import technical.task.domain.PaymentProcessingAware;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
@Component
public class ScheduledPaymentProcessor {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledPaymentProcessor.class);
    private final PaymentProcessingAware paymentService;

    public ScheduledPaymentProcessor(PaymentProcessingAware paymentService) {
        this.paymentService = paymentService;
    }

    @Scheduled(fixedRate = 60000)
    public void processDuePayments() {
        logger.info("Starting scheduled payment processing.");
        paymentService.processDuePayments();
        logger.info("Scheduled payment processing completed.");
    }
}
