package technical.task.domain.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import technical.task.domain.common.ApiUrls;
import technical.task.domain.exception.TransactionInternalServerException;
import technical.task.domain.model.transaction.TransactionCreateReq;
import technical.task.domain.model.transaction.TransactionResp;
import technical.task.domain.model.transaction.TransactionSearch;
import technical.task.domain.model.transaction.TransactionUpdateReq;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static technical.task.domain.client.ClientUtils.buildSearchUrl;
import static technical.task.domain.client.ClientUtils.buildURIWithId;
import static technical.task.domain.client.ClientUtils.getHttpEntity;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
@Component
public class TransactionClient {
    private static final Logger logger = LoggerFactory.getLogger(TransactionClient.class);
    private final RestTemplate restTemplate;
    private final String transactionBaseUrl;

    public TransactionClient(RestTemplate restTemplate, ApiUrls apiUrls) {
        this.restTemplate = restTemplate;
        this.transactionBaseUrl = apiUrls.getTransactionBaseUrl();
    }

    public TransactionResp create(TransactionCreateReq req) {
        try {
            logger.info("[RestClient] [TransactionClient#create] with req: {}", req);
            TransactionResp transactionResp = restTemplate.postForObject(transactionBaseUrl, req, TransactionResp.class);
            logger.info("[RestClient] [TransactionClient#create] transaction resp: {}", transactionResp);
            return transactionResp;
        } catch (Exception e) {
            String msg = String.format("[RestClient] [TransactionClient#create] error: %s", e);
            logger.error(msg);
            throw new TransactionInternalServerException(msg);
        }
    }

    public TransactionResp update(TransactionUpdateReq req, Long id) {
        logger.info("[RestClient] [TransactionClient#update] with req: {} and id: {}", req, id);
        try {
            ResponseEntity<TransactionResp> resp = restTemplate.exchange(
                    buildURIWithId(transactionBaseUrl, id),
                    HttpMethod.PATCH,
                    getHttpEntity(req),
                    TransactionResp.class
            );
            TransactionResp body = resp.getBody();
            logger.info("[RestClient] [TransactionClient#update] transaction resp: {}", body);
            return body;
        } catch (Exception e) {
            String msg = String.format("[RestClient] [TransactionClient#update] error: %s", e);
            logger.error(msg);
            throw new TransactionInternalServerException(msg);
        }
    }

    public TransactionResp getById(Long id) {
        ResponseEntity<TransactionResp> entity = getByIdEntity(id);
        if (entity.getStatusCode().is5xxServerError()) {
            throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (entity.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return entity.getBody();
    }

    public List<TransactionResp> search(TransactionSearch req) {
        try {
            logger.info("[RestClient] [TransactionClient#search] with req: {}", req);
            TransactionResp[] arr = restTemplate.getForObject(buildSearchUrl(transactionBaseUrl, req), TransactionResp[].class);
            List<TransactionResp> respList = arr == null ? Collections.emptyList() : Arrays.asList(arr);
            logger.info("[RestClient] [TransactionClient#search] transaction resp size: {}", respList.size());
            return respList;
        } catch (Exception e) {
            String msg = String.format("[RestClient] [TransactionClient#search] error: %s", e);
            logger.error(msg);
            throw new TransactionInternalServerException(msg);
        }
    }

    private ResponseEntity<TransactionResp> getByIdEntity(Long id) {
        try {
            logger.info("[RestClient] [TransactionClient#getById] with id: {}", id);
            ResponseEntity<TransactionResp> responseEntity = restTemplate.getForEntity(buildURIWithId(transactionBaseUrl, id), TransactionResp.class);
            logger.info("[RestClient] [TransactionClient#getById] transaction resp: {}", responseEntity);
            return responseEntity;
        } catch (HttpClientErrorException.NotFound e) {
            String msg = String.format("[RestClient] [TransactionClient#getById] payment with id: %s not found, error: %s", id, e);
            logger.error(msg);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            String msg = String.format("[RestClient] [TransactionClient#getById] error: %s", e);
            logger.error(msg);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
