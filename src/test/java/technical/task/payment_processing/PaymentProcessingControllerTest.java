package technical.task.payment_processing;

import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.web.client.MockRestServiceServer;
import technical.task.domain.exception.DebitPaymentBadRequestException;
import technical.task.domain.model.payment_instruction.PaymentInstructionReq;
import technical.task.domain.model.payment_instruction.PaymentInstructionResp;
import technical.task.domain.model.transaction.TransactionResp;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static technical.task.domain.client.ClientUtils.buildURIWithId;
import static technical.task.domain.common.Constants.PAYMENT_INSTRUCTION_BASE_URL;
import static technical.task.domain.common.Constants.TRANSACTION_BASE_URL;
import static technical.task.domain.common.JsonUtils.toJson;
import static technical.task.domain.model.PaymentInstructionTestData.createPaymentInstructionReq;
import static technical.task.domain.model.PaymentInstructionTestData.createPaymentInstructionResp;
import static technical.task.domain.model.TransactionTestData.createTransactionResp1;
import static technical.task.domain.model.TransactionTestData.createTransactionUpdateReq;
import static technical.task.domain.model.TransactionTestData.updatedStornedTransactionResp;

@Component
public class PaymentProcessingControllerTest {
    @Autowired
    PaymentProcessingController paymentProcessingController;

    public void createPaymentTest(MockRestServiceServer mockServer) {
        PaymentInstructionReq req = createPaymentInstructionReq();
        PaymentInstructionResp expectedResp = createPaymentInstructionResp();

        // mock external rest call to dao service(payment - create)
        mockServer.expect(requestTo(PAYMENT_INSTRUCTION_BASE_URL))
                .andExpect(method(HttpMethod.POST))
                .andExpect(content().json(toJson(req)))
                .andRespond(withSuccess(toJson(expectedResp), MediaType.APPLICATION_JSON));

        ResponseEntity<PaymentInstructionResp> responseEntity = paymentProcessingController.create(req);
        PaymentInstructionResp actualResp = responseEntity.getBody();
        Assertions.assertThat(actualResp).isEqualTo(expectedResp);
    }

    public void debitPaymentTest(MockRestServiceServer mockServer) {
        PaymentInstructionResp resp = createPaymentInstructionResp();
        TransactionResp transactionResp1 = createTransactionResp1(resp);

        // mock external rest calls to dao service(payment - getById)
        mockServer.expect(requestTo(buildURIWithId(PAYMENT_INSTRUCTION_BASE_URL, resp.id())))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(toJson(resp), MediaType.APPLICATION_JSON));

        // mock external rest calls to dao service(transaction - getByPaymentId)
        mockServer.expect(requestTo(containsString(TRANSACTION_BASE_URL)))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(toJson(List.of(transactionResp1)), MediaType.APPLICATION_JSON));

        ResponseEntity<TransactionResp> responseEntity;
        try {
            responseEntity = paymentProcessingController.debitPayment(resp.id());
            Assertions.fail("Expected DebitPaymentBadRequestException to be thrown");
        } catch (DebitPaymentBadRequestException e) {
            responseEntity = null;
        }
        Assertions.assertThat(responseEntity).isNull();
    }

    public void markTransactionAsStornedTest(MockRestServiceServer mockServer) {
        PaymentInstructionResp resp = createPaymentInstructionResp();
        TransactionResp transactionResp1 = createTransactionResp1(resp);
        Long transactionId = transactionResp1.id();
        TransactionResp updatedStornedTransactionResp = updatedStornedTransactionResp(transactionResp1);

        // mock external rest calls to dao service(transaction - update)
        mockServer.expect(requestTo(buildURIWithId(TRANSACTION_BASE_URL, transactionId)))
                .andExpect(method(HttpMethod.PATCH))
                .andExpect(content().json(toJson(createTransactionUpdateReq())))
                .andRespond(withSuccess(toJson(updatedStornedTransactionResp), MediaType.APPLICATION_JSON));

        ResponseEntity<TransactionResp> responseEntity = paymentProcessingController.markTransactionAsStorned(transactionId);
        Assertions.assertThat(responseEntity.getBody()).isEqualTo(updatedStornedTransactionResp);
    }
}
