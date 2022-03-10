package me.louisdefromont;

import org.springframework.web.client.RestTemplate;

public class RepeatableEventRepository {
    private String schedulePlannerBackEndURL;
    private RestTemplate restTemplate;

    public RepeatableEventRepository(String schedulePlannerBackEndURL) {
        this.schedulePlannerBackEndURL = schedulePlannerBackEndURL;
        restTemplate = new RestTemplate();
    }

    public PlannedEvent createPlannedEvent(PlannedEvent plannedEvent) {
        return restTemplate.postForObject(schedulePlannerBackEndURL + "/events/plannedEvents", plannedEvent, PlannedEvent.class);
    }
}
