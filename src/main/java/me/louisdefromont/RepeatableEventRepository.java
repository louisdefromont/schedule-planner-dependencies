package me.louisdefromont;

import org.springframework.web.client.RestTemplate;

public class RepeatableEventRepository extends EventRepository<RepeatableEvent> {
    public RepeatableEventRepository(String schedulePlannerBackEndURL) {
        super(schedulePlannerBackEndURL, "/repeatableEvents", RepeatableEvent.class);
    }
}
