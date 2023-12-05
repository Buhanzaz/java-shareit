package shareit.geteway.webClient;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

public class BasicWebClient {

    RestTemplate restTemplate;

    public BasicWebClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T> ResponseEntity<?> post(T t) {
        return post(null, null, t, null);
    }

    protected <T> ResponseEntity<?> post(String uri, @Nullable Map<String, Object> param, T t, Long userId) {
        return generatorRequest(uri, HttpMethod.POST, param, t, userId);
    }

    protected <T> ResponseEntity<?> update(String uri, Long userId, T t) {
        return update(uri, null, t, userId);
    }

    protected <T> ResponseEntity<?> update(String uri, @Nullable Map<String, Object> param, T t, Long userId) {
        return generatorRequest(uri, HttpMethod.PATCH, param, t, userId);
    }

    protected <T> void delete(String uri, Long userId) {
        delete(uri, null, null, userId);
    }

    protected <T> void delete(String uri, @Nullable Map<String, Object> param, T t, Long userId) {
        generatorRequest(uri, HttpMethod.DELETE, param, t, userId);
    }

    protected <T> ResponseEntity<?> get(String uri) {
        return get(uri, null, null, null);
    }

    protected <T> ResponseEntity<?> get(String uri, Long userId) {
        return get(uri, null, null, userId);
    }

    protected <T> ResponseEntity<?> get(String uri, @Nullable Map<String, Object> param, T t, Long userId) {
        return generatorRequest(uri, HttpMethod.GET, param, t, userId);
    }

    private <T> ResponseEntity<?> generatorRequest(String uri, HttpMethod method, @Nullable Map<String, Object> param, T t, Long userId) {
        HttpEntity<T> httpEntity = new HttpEntity<>(t, defaultHeaders(userId));
        ResponseEntity<?> exchange;
        try {
            if (param != null) {
                exchange = restTemplate.exchange(uri, method, httpEntity, Object.class, param);
            } else {
                exchange = restTemplate.exchange(uri, method, httpEntity, Object.class);
            }
        } catch (HttpStatusCodeException exception) {
            return ResponseEntity.status(exception.getStatusCode()).body(exception.getResponseBodyAsByteArray());
        }

        return exchange;
    }

    private HttpHeaders defaultHeaders(Long userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        if (userId != null) {
            headers.set("X-Sharer-User-Id", String.valueOf(userId));
        }
        return headers;
    }
}

