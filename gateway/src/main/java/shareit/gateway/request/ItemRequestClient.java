package shareit.gateway.request;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.gateway.request.dto.ItemRequestDto;
import shareit.gateway.webClient.BasicWebClient;

import java.util.Map;

@Component
public class ItemRequestClient extends BasicWebClient {

    private static final String API_REQUEST_LOCATION = "/requests";

    public ItemRequestClient(@Value("${api.shareIt.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_REQUEST_LOCATION))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<?> addItemRequest(Long creatorId, ItemRequestDto dto) {
        return post("", dto, creatorId);
    }

    public ResponseEntity<?> findAllItemsRequestsCreator(Long creatorId, Integer from, Integer size) {
        Map<String, Object> param = Map.of(
                "from", from,
                "size", size
        );
        return get("", param, creatorId);
    }

    public ResponseEntity<?> searchAllItemsRequests(Long userId, Integer from, Integer size) {
        Map<String, Object> param = Map.of(
                "from", from,
                "size", size
        );
        return get("/all?from={from}&size={size}", param, userId);
    }

    public ResponseEntity<?> searchAllItemsRequestsById(Long userId, Long requestId) {
        return get(String.format("/%d", requestId), userId);
    }
}
