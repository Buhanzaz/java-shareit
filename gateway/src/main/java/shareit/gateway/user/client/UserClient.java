package shareit.gateway.user.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import shareit.gateway.user.dto.UserDto;
import shareit.gateway.webClient.BasicWebClient;

@Component
public class UserClient extends BasicWebClient {
    private static final String API_USER_LOCATION = "/users";

    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_USER_LOCATION))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new).build());
    }

    public ResponseEntity<?> addUser(UserDto dto) {
        return post(dto);
    }

    public ResponseEntity<?> updateUser(Long userId, UserDto dto) {
        return update(String.format("/%d", userId), dto, userId);
    }

    public void deleteUser(Long userId) {
        delete(String.format("/%d", userId), userId);
    }

    public ResponseEntity<?> getUserById(Long userId) {
        return get(String.format("/%d", userId), userId);
    }

    public ResponseEntity<?> getAllUsers() {
        return get("");
    }
}
