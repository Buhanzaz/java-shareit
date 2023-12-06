package shareit.gateway.item.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.gateway.item.dto.CommentDto;
import shareit.gateway.item.dto.ItemDto;
import shareit.gateway.webClient.BasicWebClient;

import java.util.Map;

@Component
public class ItemClient extends BasicWebClient {
    private static final String API_ITEM_LOCATION = "/items";

    public ItemClient(@Value("${api.shareIt.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_ITEM_LOCATION))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<?> addItem(Long userId, ItemDto dto) {
        return post(dto, userId);
    }

    public ResponseEntity<?> updateItem(Long itemId, Long userId, ItemDto dto) {
        return update(String.format("/%d", itemId), dto, userId);
    }

    public ResponseEntity<?> getItemById(Long itemId, Long userId) {
        return get(String.format("/%d", itemId), userId);
    }

    public ResponseEntity<?> getAllItemsOwner(Long userId) {
        return get("", userId);
    }

    public ResponseEntity<?> itemSearch(String itemName, Long userId, Integer from, Integer size) {
        Map<String, Object> paramFrom = Map.of(
                "text", itemName,
                "from", from,
                "size", size
        );

        return get("/search?text={text}&from={from}&size={size}", paramFrom, userId);
    }

    public ResponseEntity<?> addComment(Long userId, Long itemId, CommentDto dto) {
        return post(String.format("/%d/comment", itemId), dto, userId);
    }
}
