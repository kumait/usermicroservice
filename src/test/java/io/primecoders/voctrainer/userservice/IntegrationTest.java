package io.primecoders.voctrainer.userservice;

import io.primecoders.voctrainer.userservice.infra.security.TokenService;
import io.primecoders.voctrainer.userservice.models.web.requests.CreateUserRequest;
import io.primecoders.voctrainer.userservice.models.web.requests.LoginRequest;
import io.primecoders.voctrainer.userservice.models.web.responses.CreateUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTest {
    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Tester";
    public static final String EMAIL = "test@test.com";
    public static final String PASSWORD = "password";
    public static final String NEW_PASSWORD = "new_password";
    public static final String LOGIN_URL = "/login";

    @LocalServerPort
    private int port;

    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private TokenService tokenService;

    private String getUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

    private HttpHeaders headers(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList("application/json"));
        if (token != null) {
            headers.put("auth-token", Collections.singletonList(token));
        }
        return headers;
    }

    @Test()
    public void testCreateUser() throws IOException {
        CreateUserRequest createUserRequest = new CreateUserRequest(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
        HttpEntity<CreateUserRequest> entity = new HttpEntity<>(createUserRequest, headers(null));
        ResponseEntity<CreateUserResponse> createUserResponseResponseEntity = restTemplate.exchange(getUrl("/users"), HttpMethod.POST, entity, CreateUserResponse.class);
        assertEquals(HttpStatus.CREATED, createUserResponseResponseEntity.getStatusCode());
        CreateUserResponse createUserResponse = createUserResponseResponseEntity.getBody();
        assertNotNull(createUserResponse);
        assertAll(() -> {
            assertEquals(createUserRequest.getUsername(), createUserResponse.getUsername());
            assertEquals(createUserRequest.getFirstName(), createUserResponse.getFirstName());
            assertEquals(createUserRequest.getLastName(), createUserResponse.getLastName());
            assertNotNull(createUserResponse.getId());
        });
    }

    @Test()
    public void testLogin() {
        ResponseEntity<Void> loginResponseEntity = restTemplate.exchange(getUrl(LOGIN_URL), HttpMethod.POST,
                new HttpEntity<>(new LoginRequest("fakeusername", "fakepassword"), headers(null)), Void.class);
        assertEquals(HttpStatus.FORBIDDEN, loginResponseEntity.getStatusCode());



    }

    /*private ResponseEntity<String> activate(EmailActivationDto emailActivationDto) {
        HttpEntity<EmailActivationDto> entity = new HttpEntity<>(emailActivationDto, headers(null));
        return restTemplate.exchange(getUrl("/api/users/status"), HttpMethod.PUT, entity, String.class);
    }

    private void resetPassword(PasswordResetDto passwordResetDto) {
        HttpEntity<PasswordResetDto> entity = new HttpEntity<>(passwordResetDto, headers(null));
        restTemplate.exchange(getUrl("/api/users/password"), HttpMethod.PUT, entity, String.class);
    }

    private ResponseEntity<String> authenticate(LoginDto loginDto) {
        HttpEntity<LoginDto> entity = new HttpEntity<>(loginDto, headers(null));
        return restTemplate.exchange(getUrl("/api/auth"), HttpMethod.POST, entity, String.class);
    }

    private EntryDto addEntry(String token, AddEntryDto addEntryDto) {
        HttpEntity<AddEntryDto> entity = new HttpEntity<>(addEntryDto, headers(token));
        return restTemplate.exchange(getUrl("/api/entries"), HttpMethod.POST, entity, EntryDto.class).getBody();
    }

    private EntryDto updateEntry(String token, long entryId, UpdateEntryDto updateEntryDto) {
        HttpEntity<UpdateEntryDto> entity = new HttpEntity<>(updateEntryDto, headers(token));
        return restTemplate.exchange(getUrl("/api/entries/" + entryId), HttpMethod.PUT, entity, EntryDto.class).getBody();
    }

    private void deleteEntry(String token, long entryId) {
        HttpEntity<PropertyDto> entity = new HttpEntity<>(null, headers(token));
        restTemplate.exchange(getUrl("/api/entries/" + entryId), HttpMethod.DELETE, entity, String.class);
    }

    private List<MinimalEntryDto> getEntries(String token, int page, int size) {
        HttpEntity<PropertyDto> entity = new HttpEntity<>(null, headers(token));
        return (List<MinimalEntryDto>) restTemplate.exchange(getUrl("/api/entries/?page=" + page + "&size=" + size), HttpMethod.GET, entity, List.class).getBody();
    }

    private PropertyDto addProperty(String token, long entryId, PropertyDto propertyDto) {
        HttpEntity<PropertyDto> entity = new HttpEntity<>(propertyDto, headers(token));
        return restTemplate.exchange(getUrl("/api/entries/" + entryId + "/props"), HttpMethod.POST, entity, PropertyDto.class).getBody();
    }

    private PropertyDto updateProperty(String token, long propertyId, PropertyDto propertyDto) {
        HttpEntity<PropertyDto> entity = new HttpEntity<>(propertyDto, headers(token));
        return restTemplate.exchange(getUrl("/api/props/" + propertyId),
                HttpMethod.PUT, entity, PropertyDto.class).getBody();
    }

    private void deleteProperty(String token, long propertyId) {
        HttpEntity<PropertyDto> entity = new HttpEntity<>(null, headers(token));
        restTemplate.exchange(getUrl("/api/props/" + propertyId), HttpMethod.DELETE, entity, String.class);
    }

    private EntryDto getEntry(String token, long entryId) {
        HttpEntity<PropertyDto> entity = new HttpEntity<>(null, headers(token));
        return restTemplate.exchange(getUrl("/api/entries/" + entryId), HttpMethod.GET, entity, EntryDto.class).getBody();
    }*/

    public void test() throws IOException {
        // create user






        /*// fail login (account not active)
        assertEquals(HttpStatus.FORBIDDEN, authenticate(new LoginDto(EMAIL, PASSWORD)).getStatusCode());

        // activate
        assertEquals(HttpStatus.OK, activate(new EmailActivationDto(EMAIL, tokenService.createActivationToken(EMAIL))).getStatusCode());
        assertEquals(HttpStatus.OK, authenticate(new LoginDto(EMAIL, PASSWORD)).getStatusCode());

        // reset password
        resetPassword(new PasswordResetDto(EMAIL, NEW_PASSWORD, tokenService.createPasswordResetToken(EMAIL)));

        // fail login (password was changed)
        assertEquals(HttpStatus.FORBIDDEN, authenticate(new LoginDto(EMAIL, PASSWORD)).getStatusCode());

        // authenticate
        ResponseEntity<String> authenticate = authenticate(new LoginDto(EMAIL, NEW_PASSWORD));
        assertEquals(HttpStatus.OK, authenticate.getStatusCode());
        String token = Objects.requireNonNull(authenticate.getHeaders().get("auth-token")).get(0);
        assertNotNull(token);

        // add entry
        AddEntryDto addEntryDto = new AddEntryDto("test text",
                Arrays.asList(
                        new AddPropertyDto(PropertyType.TAG.toString(), "added-property1"),
                        new AddPropertyDto(PropertyType.TAG.toString(), "added-property2"),
                        new AddPropertyDto(PropertyType.TAG.toString(), "added-property3")
                )
        );

        EntryDto addedEntry1 = addEntry(token, addEntryDto);
        EntryDto addedEntry2 = addEntry(token, addEntryDto);
        assertNotNull(addedEntry1);
        assertNotNull(addedEntry2);

        // update entry
        UpdateEntryDto updateEntryDto = new UpdateEntryDto("test text updated", Arrays.asList(
                new UpdatePropertyDto(1L, PropertyType.TAG.toString(), "updated-property1"),
                new UpdatePropertyDto(2L, PropertyType.TAG.toString(), "updated-property2"),
                new UpdatePropertyDto(PropertyType.LOCATION.toString(), "new-property")
        ));
        EntryDto updatedEntry = updateEntry(token, addedEntry1.getId(), updateEntryDto);
        assertNotNull(updatedEntry);

        // delete entry
        deleteEntry(token, addedEntry2.getId());

        // get entries
        List<MinimalEntryDto> savedEntries = getEntries(token, 0, 10);
        assertEquals(1, savedEntries.size());

        // add property
        PropertyDto p1 = addProperty(token, updatedEntry.getId(), new PropertyDto(PropertyType.WEATHER.toString(), "Sunny"));
        PropertyDto p2 = addProperty(token, updatedEntry.getId(), new PropertyDto(PropertyType.WEATHER.toString(), "Warm"));
        PropertyDto p3 = addProperty(token, updatedEntry.getId(), new PropertyDto(PropertyType.WEATHER.toString(), "Cold"));
        assertNotNull(p1);
        assertNotNull(p2);
        assertNotNull(p3);

        // update property
        PropertyDto updatedProperty = updateProperty(token, p2.getId(), new PropertyDto(PropertyType.WEATHER.toString(), "Cloudy"));
        assertNotNull(updatedProperty);

        // delete property
        deleteProperty(token, p3.getId());

        // get entry

        EntryDto savedEntry = getEntry(token, updatedEntry.getId());
        assertNotNull(savedEntry);

        assertEquals("test text updated", savedEntry.getText());
        assertEquals(6, savedEntry.getProperties().size());*/
    }


}
