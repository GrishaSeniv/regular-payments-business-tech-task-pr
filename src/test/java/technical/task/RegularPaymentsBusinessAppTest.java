package technical.task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import technical.task.payment_processing.PaymentProcessingControllerTest;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-05
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class RegularPaymentsBusinessAppTest {
    @Autowired
    private PaymentProcessingControllerTest paymentProcessingControllerTest;
    private MockRestServiceServer mockServer;
    @Autowired
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    public void testCreatePayment() {
        paymentProcessingControllerTest.createPaymentTest(mockServer);
    }

    @Test
    public void testDebitPayment() {
        paymentProcessingControllerTest.debitPaymentTest(mockServer);
    }

    @Test
    public void testMarkTransactionAsStorned() {
        paymentProcessingControllerTest.markTransactionAsStornedTest(mockServer);
    }
}
