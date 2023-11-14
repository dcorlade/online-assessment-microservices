package app.communication;

import org.json.JSONObject;

public class Authorisation {
    /**
     * This method sends a HTTP-request to the authorisation microservice to check if a certain
     * action is allowed.
     *
     * @param sessionToken The Java Web Token that the microservice
     *                     (should have) received as a parameter.
     * @param requiredRole The role that the user should have to perform the action. a 0 represents
     *                     a student and a 1 represents a teacher.
     * @return returns true if the action is allowed and false otherwise.
     */

    public static Boolean getAuthorisation(String sessionToken, int requiredRole) {
        String response = CourseServiceCommunication
            .getRequest("8081/authorisation/getAuthorisation/"
                + requiredRole, sessionToken);

        if (response == null) {
            return null;
        }

        Boolean result = (Boolean) new JSONObject(response).get("authorised");
        return result;
    }
}
