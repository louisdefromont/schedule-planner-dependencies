package me.louisdefromont;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class TimeBlock {
    LocalDateTime startTime;
    LocalDateTime endTime;

    public TimeBlock(LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.startTime = LocalDateTime.of(date, startTime);
        this.endTime = LocalDateTime.of(date, endTime);
    }

    public TimeBlock(LocalDateTime startTime, LocalDateTime endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public int getDurationMinutes() {
        return (int) ChronoUnit.MINUTES.between(this.startTime, this.endTime);
    }
}
