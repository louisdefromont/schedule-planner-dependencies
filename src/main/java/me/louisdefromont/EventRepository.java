package me.louisdefromont;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import kong.unirest.GenericType;
import kong.unirest.Unirest;

public class EventRepository <T extends Event> {
    private String schedulePlannerBackEndURL;
    private String endPoint;
    private Class<T> classType;
    private ObjectMapper objectMapper;

    public EventRepository(String schedulePlannerBackEndURL, String endPoint, Class<T> classType) {
        this.schedulePlannerBackEndURL = schedulePlannerBackEndURL;
        this.endPoint = endPoint;
        this.classType = classType;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public T saveEvent(T event) {
        try {
            String output = Unirest.post(schedulePlannerBackEndURL + "/events" + endPoint)
                    .header("Content-Type", "application/json")
                    .body(objectMapper.writeValueAsString(event))
                    .asString()
                    .getBody();
            return objectMapper.readValue(output, classType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Iterable<T> getAllEvents() {
        String output = Unirest.get(schedulePlannerBackEndURL + "/events" + endPoint)
                    .asString()
                    .getBody();
        try {
            return objectMapper.readValue(output, objectMapper.getTypeFactory().constructCollectionType(List.class, classType));
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public T deleteEvent(T event) {
        String output;
        try {
            output = Unirest.delete(schedulePlannerBackEndURL + "/events" + endPoint)
                    .header("Content-Type", "application/json")
                    .body(objectMapper.writeValueAsString(event))
                    .asString()
                    .getBody();
            return objectMapper.readValue(output, classType);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
