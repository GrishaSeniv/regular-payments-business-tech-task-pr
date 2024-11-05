package technical.task.domain.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import technical.task.domain.exception.PreconditionsBadRequestException;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-05
 */
public class Preconditions {
    private static final Logger logger = LoggerFactory.getLogger(Preconditions.class);

    public static void checkFullNameStr(String fullName) {
        String[] parts = fullName.trim().split("\\s+");
        if (parts.length != 3) {
            String msg = "Full name should contains firstname surname lastname";
            logger.error(msg);
            throw new PreconditionsBadRequestException(msg);
        }
    }
}
