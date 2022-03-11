package me.louisdefromont;

import java.util.List;

import kong.unirest.GenericType;
import kong.unirest.Unirest;

public class EventRepository <T extends Event> {
    private String schedulePlannerBackEndURL;
    private String endPoint;
    private Class<T> classType;
    private GenericType<List<T>> classTypeAsList;

    public EventRepository(String schedulePlannerBackEndURL, String endPoint, Class<T> classType, GenericType<List<T>> classTypeAsList) {
        this.schedulePlannerBackEndURL = schedulePlannerBackEndURL;
        this.endPoint = endPoint;
        this.classType = classType;
        this.classTypeAsList = classTypeAsList;
    }

    public T saveEvent(T event) {
        return Unirest.post(schedulePlannerBackEndURL + "/events" + endPoint)
                .header("Content-Type", "application/json")
                .body(event)
                .asObject(classType)
                .getBody();
    }
    
    public Iterable<T> getAllEvents() {
        return Unirest.get(schedulePlannerBackEndURL + "/events" + endPoint)
                .asObject(classTypeAsList)
                .getBody();
    }

    public T deleteEvent(T event) {
        return Unirest.delete(schedulePlannerBackEndURL + "/events" + endPoint)
                .header("Content-Type", "application/json")
                .body(event)
                .asObject(classType)
                .getBody();
    }
}
