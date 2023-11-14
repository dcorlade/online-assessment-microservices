package app.communication;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExamServiceCommunication {

    private static final HttpClient client = HttpClient.newBuilder().build();

    /**
     * creates and sends an http get-request to the desired endpoint.
     *
     * @param portControllerMethodPathvariables for example: "8083/exam_service/examById/21"
     * @return returns null if there has been an error. Else, returns the response in JSON format.
     */
    public static String getRequest(String portControllerMethodPathvariables, String sessionToken) {
        HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("http://localhost:" + portControllerMethodPathvariables))
            .header("session", sessionToken)
            .header("Content-Type", "application/json")
            .build();

        return serverRequest(request);
    }

    /**
     * creates and sends an http post-request to the desired endpoint.
     *
     * @param portControllerMethodPathvariables for example: "8083/exam_service/examById/21"
     * @return returns null if there has been an error. Else, returns the response in JSON format.
     */
    public static String postRequest(String dataInJson, String portControllerMethodPathvariables,
                                     String sessionToken) {
        HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(dataInJson))
            .uri(URI.create("http://localhost:" + portControllerMethodPathvariables))
            .header("Session", sessionToken)
            .header("Content-Type", "application/json")
            .build();
        return serverRequest(request);
    }

    /**
     * This function send the HttpRequest to the server and returns the server response.
     *
     * @param request HttpRequest for the server.
     * @return The body of the server response.
     */
    static String serverRequest(HttpRequest request) {
        try {
            int desiredStatusCode = 200;
            HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != desiredStatusCode) {
                System.out.println("Status: " + response.statusCode());
                System.out.println(response.body());
                return null;
            }
            return response.body();
        } catch (Exception e) {
            System.out.println("Communication with other microservice failed");
            return null;
        }
    }
}
