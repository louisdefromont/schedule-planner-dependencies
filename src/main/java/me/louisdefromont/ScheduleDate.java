package me.louisdefromont;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


/**
 * A single day in the schedule that contains a list of scheduled events.
 */
@Entity
 public class ScheduleDate {
    /**
     * The unique identifier for this schedule date.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * The date of this schedule date.
     */
    private LocalDate date;
    /**
     * The list of scheduled events for this schedule date.
     */
    @OneToMany(cascade = CascadeType.ALL)
    private List<ScheduledEvent> events;

    public ScheduleDate() {
        events = new ArrayList<ScheduledEvent>();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<ScheduledEvent> getEvents() {
        return this.events;
    }

    public void setEvents(List<ScheduledEvent> events) {
        this.events = events;
    }

    // TODO: Proper time overlap checking
    public void addEvent(ScheduledEvent event) {
        this.events.add(event);
        this.events.sort((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()));
    }

    public int getAvailiableMinutes() {
        int minutes = 24 * 60;
        for (ScheduledEvent event : events) {
            minutes -= event.getDurationMinutes();
        }
        return minutes;
    }

    public List<TimeBlock> getAvailiableTimeBlocks() {
        List<TimeBlock> timeBlocks = new ArrayList<TimeBlock>();
        if (events.size() == 0) {
            timeBlocks.add(new TimeBlock(date, LocalTime.of(0, 0), LocalTime.of(23, 59)));
        } else {
            if (events.get(0).getStartDateTime().isAfter(LocalDateTime.of(date, LocalTime.of(0, 0)))) {
                timeBlocks.add(new TimeBlock(date, LocalTime.of(0, 0), events.get(0).getStartDateTime().toLocalTime()));
            }
            for (int scheduledEvent = 0; scheduledEvent < events.size() - 1; scheduledEvent++) {
                if (! events.get(scheduledEvent).getEndDateTime().equals(events.get(scheduledEvent + 1).getStartDateTime())) {
                    TimeBlock timeBlock = new TimeBlock(events.get(scheduledEvent).getEndDateTime(), events.get(scheduledEvent + 1).getStartDateTime());
                    timeBlocks.add(timeBlock);
                }
            }
            if (events.get(events.size() - 1).getEndDateTime().isBefore(LocalDateTime.of(date, LocalTime.of(23, 59)))) {
                timeBlocks.add(new TimeBlock(date, events.get(events.size() - 1).getEndDateTime().toLocalTime(), LocalTime.of(23, 59)));
            }
        }
        
        return timeBlocks;
    }


    public boolean scheduleEvent(Event event, int durationMinutes) {
        if (getAvailiableMinutes() < durationMinutes) {
            return false;
        }
        int durationRemaining = durationMinutes;
        while (durationMinutes > 0) {
            for (TimeBlock timeBlock : getAvailiableTimeBlocks()) {
                if (timeBlock.getDurationMinutes() >= durationRemaining) {
                    ScheduledEvent scheduledEvent = new ScheduledEvent();
                    scheduledEvent.setName(event.getName());
                    scheduledEvent.setStartDateTime(timeBlock.getStartTime());
                    scheduledEvent.setEndDateTime(timeBlock.getStartTime().plusMinutes(durationRemaining));
                    addEvent(scheduledEvent);
                    return true;
                } else {
                    ScheduledEvent scheduledEvent = new ScheduledEvent();
                    scheduledEvent.setName(event.getName());
                    scheduledEvent.setStartDateTime(timeBlock.getStartTime());
                    scheduledEvent.setEndDateTime(timeBlock.getEndTime());
                    addEvent(scheduledEvent);
                    durationRemaining -= timeBlock.getDurationMinutes();
                }
            }
        }
        return true;
    }
    
}
