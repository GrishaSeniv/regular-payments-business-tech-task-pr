package technical.task.domain.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false);

    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        ;
        try {
            logger.info("[JsonUtils#toJson] Converting object to string JSON: {}", obj);
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("[JsonUtils#toJson] Error converting object to string JSON", e);
            return null;
        }
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        if (json == null) {
            return null;
        }
        try {
            logger.info("[JsonUtils#fromJson] Converting object from string JSON: {}", json);
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
            logger.error("[JsonUtils#fromJson] Error converting object from string JSON", e);
            return null;
        }
    }
}
