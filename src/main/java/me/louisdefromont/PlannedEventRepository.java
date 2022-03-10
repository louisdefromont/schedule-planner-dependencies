package me.louisdefromont;

import org.springframework.web.client.RestTemplate;

public class PlannedEventRepository extends EventRepository<PlannedEvent> {

    public PlannedEventRepository(String schedulePlannerBackEndURL) {
        super(schedulePlannerBackEndURL, "/plannedEvents", PlannedEvent.class);
    }
}
