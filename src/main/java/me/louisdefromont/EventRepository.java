package me.louisdefromont;

import org.springframework.web.client.RestTemplate;

public abstract class EventRepository <T extends Event> {
    private String schedulePlannerBackEndURL;
    private String endPoint;
    private Class<T> classType;
    private RestTemplate restTemplate;

    public EventRepository(String schedulePlannerBackEndURL, String endPoint, Class<T> classType) {
        this.schedulePlannerBackEndURL = schedulePlannerBackEndURL;
        this.endPoint = endPoint;
        this.classType = classType;
        restTemplate = new RestTemplate();
    }

    public T createEvent(T event) {
        return restTemplate.postForObject(schedulePlannerBackEndURL + "/events" + endPoint, event, classType);
    }
}
