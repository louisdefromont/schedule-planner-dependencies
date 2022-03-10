package me.louisdefromont;

import java.util.List;

import kong.unirest.GenericType;
import kong.unirest.Unirest;

public class EventRepository <T extends Event> {
    private String schedulePlannerBackEndURL;
    private String endPoint;
    private Class<T> classType;

    public EventRepository(String schedulePlannerBackEndURL, String endPoint, Class<T> classType) {
        this.schedulePlannerBackEndURL = schedulePlannerBackEndURL;
        this.endPoint = endPoint;
        this.classType = classType;
    }

    public T saveEvent(T event) {
        return Unirest.post(schedulePlannerBackEndURL + endPoint)
                .header("Content-Type", "application/json")
                .body(event)
                .asObject(classType)
                .getBody();
    }
    
    public Iterable<T> getAllEvents() {
        return Unirest.get(schedulePlannerBackEndURL + "/events" + endPoint)
                .asObject(new GenericType<List<T>>(){})
                .getBody();
    }
}
