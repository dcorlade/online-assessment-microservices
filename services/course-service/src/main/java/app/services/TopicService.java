package app.services;

import app.communication.Authorisation;
import app.constants.Constants;
import app.json.JsonSerializerFactory;
import app.models.Topic;
import app.repositories.TopicRepository;
import app.serializerfactory.Serializer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * This class will contain all logic for the endpoints found in TopicController.
 * Every method will check whether or not the user is authorized and then perform some logic after
 * which it returns a ResponseEntity.
 */
@Service
public class TopicService {

    private final transient TopicRepository topicRepository;

    private final transient Serializer serializer = new JsonSerializerFactory().createSerializer();

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    /**
     * Given the topicId this method will return a ResponseEntity containing a
     * string representation of the Topic object. If none is found it will return
     * 404 NOT_FOUND
     *
     * @param topicId      topicId
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> getTopic(Integer topicId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        try {
            Topic topic = topicRepository.findById(topicId).orElseThrow();
            return new ResponseEntity<String>(serializer.serialize(topic), HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "There exists no topic with that id";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Given a courseId this method returns a ResponseEntity containing a string representation of
     * a JSONObject containing a JSONArray containing json representations of all Topics with that
     * courseId.
     *
     * @param courseId     courseId
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> getTopicsByCourse(Integer courseId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 0)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        try {
            Object[] topics = topicRepository.findAll().stream()
                .filter(topic -> topic.getCourseId() == courseId)
                .toArray();
            if (topics.length == 0) {
                throw new IllegalArgumentException();
            }
            JSONArray jsonArrayTopics = new JSONArray();
            for (Object t : topics) {
                Topic topic = (Topic) t;
                jsonArrayTopics.put(serializer.serialize(topic));
            }
            JSONObject jsonObjectTopics = new JSONObject();
            jsonObjectTopics.put("topics", jsonArrayTopics);
            return new ResponseEntity<String>(jsonObjectTopics.toString(), HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "There exist no topics with that courseId";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Given an Topic object it will attempt to store this in the database. If a topic with the
     * same id already exists it will not store it.
     *
     * @param topic        topic
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> addTopic(Topic topic, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }
        topicRepository.save(topic);
        return new ResponseEntity<String>(serializer.serialize(topic), HttpStatus.OK);
    }

    /**
     * Given a Topic object it will attempt to update this in the database. If no question with the
     * id exists it will return 404 NOT_FOUND.
     *
     * @param topic        topic
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> updateTopic(Topic topic, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        try {
            if (topicRepository.findById(topic.getId()).isEmpty()) {
                throw new IllegalArgumentException();
            }
            topicRepository.save(topic);
            return new ResponseEntity<String>("Update was successful", HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "There exists no topic with that id";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Given a topicId this method will attempt to remove the related topic from the database.
     * If no topic with that id exists 404 NOT_FOUND will be returned.
     *
     * @param topicId      topicId
     * @param sessionToken sessionToken
     * @return ResponseEntity
     */
    public ResponseEntity<String> deleteTopic(Integer topicId, String sessionToken) {
        if (!Authorisation.getAuthorisation(sessionToken, 1)) {
            String errorMessage = Constants.NOT_AUTHORIZED_STRING;
            return new ResponseEntity<String>(errorMessage, HttpStatus.FORBIDDEN);
        }

        try {
            if (topicRepository.findById(topicId).isEmpty()) {
                throw new IllegalArgumentException();
            }
            topicRepository.deleteById(topicId);
            return new ResponseEntity<String>("Deletion was successful", HttpStatus.OK);
        } catch (Exception exception) {
            String errorMessage = "There exists no topic with that id";
            System.out.println(errorMessage);
            return new ResponseEntity<String>(errorMessage, HttpStatus.NOT_FOUND);
        }
    }
}