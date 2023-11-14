package app.communication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockserver.model.HttpError.error;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.StringBody.exact;

import app.constants.Constants;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;

/**
 * These tests cover the communication class in every microservice,
 * by mocking the response another microservice would provide in production.
 * These are unit tests, not integration tests: We are not interested in the
 * contents of the http requests and responses, only whether they are correctly
 * transmitted by the communication classes, and what happens when exceptions occur.
 */
class TeacherServiceCommunicationTest {

    private static ClientAndServer mockServer;
    private static HttpRequest request;
    private static URI uri;

    /**
     * A new instance of ClientAndServer is run before every test,
     * and terminated after every test. This ensures that test cases are
     * atomic and cannot interfere with each other.
     * The http request is set up both for calling the tested methods and for setting
     * up the expectations of the mock server.
     */
    @BeforeEach
    void setUp() {
        mockServer = ClientAndServer.startClientAndServer(Constants.PORT8083);

        try {
            uri = new URI("http://localhost:8083/test");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        String requestBody = "{'request: foo'}";

        request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(requestBody))
            .uri(uri)
            .setHeader("CONTENT_TYPE", "application/json")
            .build();
    }

    @AfterEach
    void tearDown() {
        mockServer.stop();
    }

    /**
     * Tests whether a successful request resulting in a perfect
     * response (Status 200 OK) is correctly returned by serverRequest.
     */
    @Test
    void success200Test() {
        new MockServerClient(Constants.HOST, Constants.PORT8083)
            .when(
                request()
                    .withMethod("POST")
                    .withPath("/test")
                    .withHeader("CONTENT_TYPE", "application/json")
                    .withBody(exact("{'request: foo'}"))
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE))
                    .withBody(Constants.RESPONSE_BODY));

        assertEquals(Constants.RESPONSE_BODY, TeacherServiceCommunication.serverRequest(request));
    }

    /**
     * Tests whether a request resulting in the wrong status response returns null.
     * Since we are not interested in any particular error, and to prevent confusion with
     * integration tests, the status code 418 (I'm a teapot) is used.
     */
    @Test
    void noSuccessTest() {

        new MockServerClient(Constants.HOST, Constants.PORT8083)
            .when(
                request()
                    .withMethod("POST")
                    .withPath("/test")
                    .withHeader("CONTENT_TYPE", "application/json")
                    .withBody(exact("{'request: foo'}"))
            )
            .respond(
                response()
                    .withStatusCode(418)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE))
                    .withBody(Constants.RESPONSE_BODY));

        assertNull(TeacherServiceCommunication.serverRequest(request));
    }

    /**
     * Mocks a dropped connection due to internet outage,
     * server off, other microservice not running, etc.
     */
    @Test
    void exceptionTest() {
        new MockServerClient(Constants.HOST, Constants.PORT8083)
            .when(
                request()
                    .withPath("/test")
            )
            .error(
                error()
                    .withDropConnection(true));

        TeacherServiceCommunication.serverRequest(request);
        assertNull(TeacherServiceCommunication.serverRequest(request));
    }

    /**
     * Tests a successful get request to the exam microservice.
     */
    @Test
    void getRequestSuccessTest() {
        new MockServerClient(Constants.HOST, Constants.PORT8083)
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/exam_service/examById/22")
                    .withHeader(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE))
                    .withBody(Constants.RESPONSE_BODY));

        assertEquals(Constants.RESPONSE_BODY, TeacherServiceCommunication
            .getRequest("8083/exam_service/examById/22", Constants.TEST_SESSION_TOKEN));
    }

    /**
     * Tests whether a get request resulting in the wrong status response returns null.
     * Since we are not interested in any particular error, and to prevent confusion with
     * integration tests, the status code 418 (I'm a teapot) is used.
     */
    @Test
    void getRequestNullTest() {
        new MockServerClient(Constants.HOST, Constants.PORT8083)
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/exam_service/examById/23")
                    .withHeader(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
            )
            .respond(
                response()
                    .withStatusCode(418)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE),
                        new Header("Cache-Control",
                            "public, max-age=86400"))
                    .withBody(Constants.RESPONSE_BODY));

        assertNull(TeacherServiceCommunication
            .getRequest("8083/exam_service/examById/23", Constants.TEST_SESSION_TOKEN));
    }

    /**
     * Tests a successful post request to the exam microservice.
     */
    @Test
    void postRequestSuccessTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("id", 23);
        String jsonString = jsonObject.toString();

        new MockServerClient(Constants.HOST, Constants.PORT8083)
            .when(
                request()
                    .withMethod("POST")
                    .withPath("/exam_service/examById/21")
                    .withHeader(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
            )
            .respond(
                response()
                    .withStatusCode(200)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE),
                        new Header("Cache-Control",
                            "public, max-age=86400"))
                    .withBody(Constants.RESPONSE_BODY));

        assertEquals(Constants.RESPONSE_BODY, TeacherServiceCommunication
            .postRequest(jsonString, "8083/exam_service/examById/21",
                Constants.TEST_SESSION_TOKEN));
    }

    /**
     * Tests whether a post request resulting in the wrong status response returns null.
     * Since we are not interested in any particular error, and to prevent confusion with
     * integration tests, the status code 418 (I'm a teapot) is used.
     */
    @Test
    void postRequestNullTest() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.append("id", 23);
        String jsonString = jsonObject.toString();

        new MockServerClient(Constants.HOST, Constants.PORT8083)
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/exam_service/examById/21")
                    .withHeader(Constants.SESSIONHEADERKEY, Constants.TEST_SESSION_TOKEN)
            )
            .respond(
                response()
                    .withStatusCode(418)
                    .withHeaders(
                        new Header(Constants.RESPONSE_HEADER_NAME,
                            Constants.RESPONSE_HEADER_VALUE),
                        new Header("Cache-Control",
                            "public, max-age=86400"))
                    .withBody(Constants.RESPONSE_BODY));

        assertNull(TeacherServiceCommunication
            .postRequest(jsonString, "8083/exam_service/examById/21",
                Constants.TEST_SESSION_TOKEN));
    }
}