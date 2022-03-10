package me.louisdefromont;

import org.springframework.web.client.RestTemplate;

public abstract class EditableEventRepository <T extends Event> {
    private String schedulePlannerBackEndURL;
    private RestTemplate restTemplate;

    public EditableEventRepository(String schedulePlannerBackEndURL) {
        this.schedulePlannerBackEndURL = schedulePlannerBackEndURL;
        restTemplate = new RestTemplate();
    }

    public PlannedEvent createEditableEvent(PlannedEvent plannedEvent) {
        return restTemplate.postForObject(schedulePlannerBackEndURL + "/events/plannedEvents", plannedEvent, PlannedEvent.class);
    }
}
