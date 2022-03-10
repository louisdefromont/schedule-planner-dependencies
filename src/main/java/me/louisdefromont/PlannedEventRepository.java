package me.louisdefromont;

import org.springframework.web.client.RestTemplate;

public class PlannedEventRepository {
    private String schedulePlannerBackEndURL;
    private RestTemplate restTemplate;

    public PlannedEventRepository(String schedulePlannerBackEndURL) {
        this.schedulePlannerBackEndURL = schedulePlannerBackEndURL;
        restTemplate = new RestTemplate();
    }

    public PlannedEvent createPlannedEvent(PlannedEvent plannedEvent) {
        return restTemplate.postForObject(schedulePlannerBackEndURL + "/events/plannedEvents", plannedEvent, PlannedEvent.class);
    }
}
