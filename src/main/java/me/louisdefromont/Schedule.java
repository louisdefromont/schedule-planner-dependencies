package me.louisdefromont;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL)
    private List<ScheduleDate> scheduleDates;
    private LocalDate startDate;
    private LocalDate endDate;

    public Schedule() {
    }

    public Schedule(LocalDate endDate, Iterable<PlannedEvent> plannedEvents, Iterable<RepeatableEvent> repeatableEvents, Iterable<ToDoEvent> toDoEvents) {
        this.startDate = LocalDate.now();
        this.endDate = endDate;
        this.scheduleDates = new ArrayList<ScheduleDate>();
        for (LocalDate date = startDate; date.isBefore(endDate.plusDays(1)); date = date.plusDays(1)) {
            ScheduleDate scheduleDate = new ScheduleDate();
            scheduleDate.setDate(date);
            scheduleDates.add(scheduleDate);
        }
        generate(plannedEvents, repeatableEvents, toDoEvents);
    }

    public List<ScheduleDate> getScheduleDates() {
        return this.scheduleDates;
    }

    public void setScheduleDates(List<ScheduleDate> scheduleDates) {
        this.scheduleDates = scheduleDates;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }



    public void addToSchedule(ScheduledEvent scheduledEvent) {
        if (scheduledEvent.getEndTime().isBefore(endDate.atTime(23, 59))) {
            scheduleDates.get((int) ChronoUnit.DAYS.between(startDate, scheduledEvent.getStartTime().toLocalDate())).addEvent(scheduledEvent);
        }
    }

    private void generate(Iterable<PlannedEvent> plannedEvents, Iterable<RepeatableEvent> repeatableEvents, Iterable<ToDoEvent> toDoEvents) {  
        for (PlannedEvent plannedEvent : plannedEvents) {
            ScheduledEvent scheduledEvent = new ScheduledEvent();
            scheduledEvent.setStartTime(plannedEvent.getStartTime());
            scheduledEvent.setEndTime(plannedEvent.getEndTime());
            scheduledEvent.setEvent(plannedEvent.getEvent());
            addToSchedule(scheduledEvent);
        }

        for (RepeatableEvent repeatableEvent : repeatableEvents) {
            // TODO: Set currentDate to latest between startDate and repeatableEvent.getStartDate()
            for (LocalDate currentDate = repeatableEvent.getStartDate(); currentDate.isBefore(endDate.plusDays(1)); currentDate = currentDate.plusDays(repeatableEvent.getRepeatInterval())) {
                if (currentDate.isAfter(startDate)) {
                    ScheduledEvent scheduledEvent = new ScheduledEvent();
                    scheduledEvent.setStartTime(LocalDateTime.of(currentDate, repeatableEvent.getStartTime()));
                    scheduledEvent.setEndTime(LocalDateTime.of(currentDate, repeatableEvent.getEndTime()));
                    scheduledEvent.setEvent(repeatableEvent.getEvent());
                    addToSchedule(scheduledEvent);
                }
            }
        }

        for (ToDoEvent toDoEvent : toDoEvents) {
            int daysLeft = (int) ChronoUnit.DAYS.between(startDate, toDoEvent.getDueDateTime().toLocalDate());
            int minutesLeft = toDoEvent.getEstimatedMinutes();
            int minutesPerDay = (int) Math.ceil((double) minutesLeft / daysLeft);
            int[] availiableMinutersPerDay = new int[daysLeft];
            while (minutesLeft > 0) {
                for (int i = 0; i < availiableMinutersPerDay.length; i++) {
                    availiableMinutersPerDay[i] = scheduleDates.get(i).getAvailiableMinutes();
                }
                daysLeft = Arrays.stream(availiableMinutersPerDay).filter(x -> x > 0).toArray().length;
                minutesPerDay = (int) Math.ceil((double) minutesLeft / daysLeft);
                for (int i = 0; i < availiableMinutersPerDay.length; i++) {
                    if (availiableMinutersPerDay[i] > 0) {
                        int minutesSpent = Math.min(minutesPerDay, availiableMinutersPerDay[i]);
                        scheduleDates.get(i).scheduleEvent(toDoEvent.getEvent(), minutesSpent);
                        minutesLeft -= minutesSpent;
                    }
                }
            }
        }

    }
}
