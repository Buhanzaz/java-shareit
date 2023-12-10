package shareit.gateway.webClient;

import org.springframework.http.*;
import org.springframework.lang.Nullable;
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
        return post("", t, null);
    }

    protected <T> ResponseEntity<?> post(T t, Long userId) {
        return post("", t, userId);
    }

    protected <T> ResponseEntity<?> post(String uri, T t, Long userId) {
        return generatorRequest(uri, HttpMethod.POST, null, t, userId);
    }

    protected ResponseEntity<?> update(String uri, Long userId) {
        return update(uri, null, userId);
    }

    protected <T> ResponseEntity<?> update(String uri, T t, Long userId) {
        return generatorRequest(uri, HttpMethod.PATCH, null, t, userId);
    }

    protected void delete(String uri, Long userId) {
        generatorRequest(uri, HttpMethod.DELETE, null, null, userId);
    }

    protected ResponseEntity<?> get(String uri) {
        return get(uri, null, null);
    }

    protected ResponseEntity<?> get(String uri, Long userId) {
        return get(uri, null, userId);
    }

    protected ResponseEntity<?> get(String uri, @Nullable Map<String, Object> param, Long userId) {
        return generatorRequest(uri, HttpMethod.GET, param, null, userId);
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

