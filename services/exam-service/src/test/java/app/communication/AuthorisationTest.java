package app.communication;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import app.constants.Constants;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

/**
 * Unit tests the Authorisation class by using a mock server.
 */
class AuthorisationTest {

    private static ClientAndServer mockServer;
    private final transient JSONObject authorisedJson = new JSONObject();

    @BeforeEach
    void setUp() {
        mockServer = ClientAndServer.startClientAndServer(Constants.PORT8081);
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();

        authorisedJson.remove("authorised");
    }

    /**
     * Test whether an authorized response is received and parsed correctly.
     */
    @Test
    void authorizedTest() {
        authorisedJson.put("authorised", true);

        new MockServerClient(Constants.HOST, Constants.PORT8081)
            .when(
                request()
                    .withMethod("GET")
                    .withPath(Constants.PATH + "0")
                    .withHeader("session", Constants.TEST_SESSION_TOKEN)
                    .withHeader(Constants.RESPONSE_HEADER_NAME, "application/json")
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE))
                    .withBody(authorisedJson.toString()));

        assertNotNull(Authorisation.getAuthorisation(Constants.TEST_SESSION_TOKEN, 0));
        assertTrue(Authorisation.getAuthorisation(Constants.TEST_SESSION_TOKEN, 0));
    }

    /**
     * Tests whether an unauthorized response is received and parsed correctly.
     */
    @Test
    void unAuthorizedTest() {
        authorisedJson.put("authorised", false);

        new MockServerClient(Constants.HOST, Constants.PORT8081)
            .when(
                request()
                    .withMethod("GET")
                    .withPath(Constants.PATH + "0")
                    .withHeader("session", Constants.TEST_SESSION_TOKEN)
                    .withHeader(Constants.RESPONSE_HEADER_NAME, "application/json")
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE))
                    .withBody(authorisedJson.toString()));

        assertNotNull(Authorisation.getAuthorisation(Constants.TEST_SESSION_TOKEN, 0));
        assertFalse(Authorisation.getAuthorisation(Constants.TEST_SESSION_TOKEN, 0));
    }

    /**
     * Tests whether an unsuccessful request is handled correctly.
     */
    @Test
    void nullTest() {
        new MockServerClient(Constants.HOST, Constants.PORT8081)
            .when(
                request()
                    .withMethod("GET")
                    .withPath(Constants.PATH + "0")
                    .withHeader("session", Constants.TEST_SESSION_TOKEN)
                    .withHeader(Constants.RESPONSE_HEADER_NAME, "application/json")
            )
            .respond(
                response()
                    .withStatusCode(404)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE)));

        assertNull(Authorisation.getAuthorisation(Constants.TEST_SESSION_TOKEN, 0));
    }


}
