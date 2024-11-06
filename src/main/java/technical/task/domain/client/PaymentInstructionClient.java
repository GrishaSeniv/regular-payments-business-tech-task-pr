package technical.task.domain.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import technical.task.domain.exception.PaymentInstructionInternalServerException;
import technical.task.domain.model.payment_instruction.PaymentInstructionReq;
import technical.task.domain.model.payment_instruction.PaymentInstructionResp;
import technical.task.domain.model.payment_instruction.PaymentInstructionSearch;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static technical.task.domain.client.ClientUtils.buildSearchUrl;
import static technical.task.domain.client.ClientUtils.buildURIWithId;
import static technical.task.domain.client.ClientUtils.getHttpEntity;
import static technical.task.domain.common.Constants.PAYMENT_INSTRUCTION_BASE_URL;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
@Component
public class PaymentInstructionClient {
    private static final Logger logger = LoggerFactory.getLogger(PaymentInstructionClient.class);
    private final RestTemplate restTemplate;

    public PaymentInstructionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PaymentInstructionResp create(PaymentInstructionReq req) {
        try {
            logger.info("[RestClient] [PaymentInstructionClient#create] with req: {}", req);
            PaymentInstructionResp resp = restTemplate.postForObject(PAYMENT_INSTRUCTION_BASE_URL, req, PaymentInstructionResp.class);
            logger.info("[RestClient] [PaymentInstructionClient#create] response: {}", resp);
            return resp;
        } catch (Exception e) {
            String msg = String.format("[RestClient] [PaymentInstructionClient#create] error: %s", e);
            logger.error(msg);
            throw new PaymentInstructionInternalServerException(msg);
        }
    }

    public PaymentInstructionResp update(PaymentInstructionReq req, Long id) {
        logger.info("[RestClient] [PaymentInstructionClient#update] with req: {} and id: {}", req, id);
        try {
            ResponseEntity<PaymentInstructionResp> resp = restTemplate.exchange(
                    buildURIWithId(PAYMENT_INSTRUCTION_BASE_URL, id),
                    HttpMethod.PUT,
                    getHttpEntity(req),
                    PaymentInstructionResp.class
            );
            PaymentInstructionResp body = resp.getBody();
            logger.info("[RestClient] [PaymentInstructionClient#update] response: {}", body);
            return body;
        } catch (Exception e) {
            String msg = String.format("[RestClient] [PaymentInstructionClient#update] error: %s", e);
            logger.error(msg);
            throw new PaymentInstructionInternalServerException(msg);
        }
    }

    public PaymentInstructionResp getById(Long id) {
        ResponseEntity<PaymentInstructionResp> entity = getByIdEntity(id);
        if (entity.getStatusCode().is5xxServerError()) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (entity.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return entity.getBody();
    }

    public List<PaymentInstructionResp> search(PaymentInstructionSearch req) {
        try {
            logger.info("[RestClient] [PaymentInstructionClient#search] with req: {}", req);
            PaymentInstructionResp[] restArray = restTemplate.getForObject(buildSearchUrl(PAYMENT_INSTRUCTION_BASE_URL, req), PaymentInstructionResp[].class);
            List<PaymentInstructionResp> respList = restArray == null ? Collections.emptyList() : Arrays.asList(restArray);
            logger.info("[RestClient] [PaymentInstructionClient#search] response size: {}", respList.size());
            return respList;
        } catch (Exception e) {
            String msg = String.format("[RestClient] [PaymentInstructionClient#search] error: %s", e);
            logger.error(msg);
            throw new PaymentInstructionInternalServerException(msg);
        }
    }

    private ResponseEntity<PaymentInstructionResp> getByIdEntity(Long id) {
        try {
            logger.info("[RestClient] [PaymentInstructionClient#getById] with id: {}", id);
            ResponseEntity<PaymentInstructionResp> response = restTemplate.getForEntity(buildURIWithId(PAYMENT_INSTRUCTION_BASE_URL, id), PaymentInstructionResp.class);
            logger.info("[RestClient] [PaymentInstructionClient#getById] response: {}", response);
            return response;
        } catch (HttpClientErrorException.NotFound e) {
            String msg = String.format("[RestClient] [PaymentInstructionClient#getById] payment with id: %s not found, error: %s", id, e);
            logger.error(msg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            String msg = String.format("[RestClient] [PaymentInstructionClient#getById] error: %s", e);
            logger.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
