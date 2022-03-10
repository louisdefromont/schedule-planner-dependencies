package me.louisdefromont;

public class ToDoEventRepository extends EventRepository<ToDoEvent> {
    public ToDoEventRepository(String schedulePlannerBackEndURL) {
        super(schedulePlannerBackEndURL, "/toDoEvents", ToDoEvent.class);
    }
}
    
