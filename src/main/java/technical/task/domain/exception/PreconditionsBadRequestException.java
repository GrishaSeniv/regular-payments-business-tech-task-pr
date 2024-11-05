package technical.task.domain.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-05
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PreconditionsBadRequestException extends RuntimeException {
    public PreconditionsBadRequestException(String message) {
        super(message);
    }
}
