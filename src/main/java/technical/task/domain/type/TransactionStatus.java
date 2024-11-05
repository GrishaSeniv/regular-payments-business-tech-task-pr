package technical.task.domain.type;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public enum TransactionStatus {
    A("active"),
    S("storned");

    private static final Logger logger = LoggerFactory.getLogger(TransactionStatus.class);
    private final String full;

    TransactionStatus(String full) {
        this.full = full;
    }

    public String getFull() {
        return String.valueOf(full);
    }
}
