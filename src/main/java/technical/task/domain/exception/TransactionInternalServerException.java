package technical.task.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-05
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class TransactionInternalServerException extends RuntimeException {
    public TransactionInternalServerException(String message) {
        super(message);
    }
}
