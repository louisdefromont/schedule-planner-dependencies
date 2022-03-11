package me.louisdefromont;

import kong.unirest.Unirest;

public class RemoteScheduleGenerator {
    private String schedulePlannerBackEndURL;
    public RemoteScheduleGenerator(String schedulePlannerBackEndURL) {
        this.schedulePlannerBackEndURL = schedulePlannerBackEndURL;
    }

    public Schedule generate() {
        return Unirest.post(schedulePlannerBackEndURL + "/schedule/generate")
                .header("Content-Type", "application/json")
                .asObject(Schedule.class)
                .getBody();
    }
}
