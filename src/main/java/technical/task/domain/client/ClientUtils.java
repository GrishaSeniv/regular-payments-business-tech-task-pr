package technical.task.domain.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import technical.task.domain.SearchAware;

import java.net.URI;
import java.util.Map;

import static technical.task.domain.common.JsonUtils.fromJson;
import static technical.task.domain.common.JsonUtils.toJson;

/**
 * @author Hryhorii Seniv
 * @version 2024-11-04
 */
public class ClientUtils {
    private static final Logger logger = LoggerFactory.getLogger(ClientUtils.class);

    public static <T> HttpEntity<T> getHttpEntity(T req) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(req, headers);
    }

    public static URI buildURIWithId(String baseUrl, Long id) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .pathSegment(String.valueOf(id))
                .build()
                .toUri();
    }

    public static URI buildSearchUrl(String baseUrl, SearchAware search) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl);
        try {
            String jsonString = toJson(search);
            Map<String, Object> params = fromJson(jsonString, Map.class);
            params.forEach((key, value) -> {
                if (value != null) {
                    builder.queryParam(key, value.toString());
                }
            });
            return builder.build().toUri();
        } catch (Exception e) {
            logger.error("Couldn't build search url", e);
            return builder.build().toUri();
        }
    }
}
