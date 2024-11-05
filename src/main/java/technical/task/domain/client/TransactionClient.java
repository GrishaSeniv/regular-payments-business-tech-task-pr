package technical.task.domain.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
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
import static technical.task.domain.common.Constants.TRANSACTION_BASE_URL;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
@Component
public class TransactionClient {
    private static final Logger logger = LoggerFactory.getLogger(TransactionClient.class);
    private final RestTemplate restTemplate;

    public TransactionClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TransactionResp create(TransactionCreateReq req) {
        try {
            logger.info("[RestClient] [TransactionClient#create] with req: {}", req);
            return restTemplate.postForObject(TRANSACTION_BASE_URL, req, TransactionResp.class);
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
                    buildURIWithId(TRANSACTION_BASE_URL, id),
                    HttpMethod.PATCH,
                    getHttpEntity(req),
                    TransactionResp.class
            );
            return resp.getBody();
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
            TransactionResp[] arr = restTemplate.getForObject(buildSearchUrl(TRANSACTION_BASE_URL, req), TransactionResp[].class);
            return arr == null ? Collections.emptyList() : Arrays.asList(arr);
        } catch (Exception e) {
            String msg = String.format("[RestClient] [TransactionClient#search] error: %s", e);
            logger.error(msg);
            throw new TransactionInternalServerException(msg);
        }
    }

    private ResponseEntity<TransactionResp> getByIdEntity(Long id) {
        try {
            logger.info("[RestClient] [TransactionClient#getById] with id: {}", id);
            return restTemplate.getForEntity(buildURIWithId(TRANSACTION_BASE_URL, id), TransactionResp.class);
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
