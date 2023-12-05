package shareit.geteway.user;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shareit.geteway.user.dto.UserDto;

import java.util.List;

@Service
public class UserClient {
    private static final String API_PREFIX = "/users";

    @Value("${api.shareIt.server.url}")
    String serverUrl;

    RestTemplate restTemplate;

    public UserClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ResponseEntity<?> addUser(UserDto dto) {
        return ResponseEntity.ok(restTemplate.postForObject(serverUrl + API_PREFIX, dto, UserDto.class));
    }

    public ResponseEntity<?> getUserById(Long userId) {
        return ResponseEntity.ok(restTemplate.getForObject(serverUrl + API_PREFIX + "/" + userId.toString(), UserDto.class));
    }

    public void deleteUser(Long userId) {
        restTemplate.delete(serverUrl + API_PREFIX + "/" + userId.toString());
    }

    public ResponseEntity<?> updateUser(Long userId, UserDto dto) {
        HttpEntity<UserDto> entity = new HttpEntity<>(dto);
        return restTemplate.exchange(serverUrl + API_PREFIX + "/" + userId, HttpMethod.PATCH, entity, UserDto.class);
    }

    public ResponseEntity<?> getAllUsers() {
        return restTemplate.exchange(serverUrl + API_PREFIX,
                        HttpMethod.GET, null, new ParameterizedTypeReference<List<UserDto>>() {
                        });
    }
}
