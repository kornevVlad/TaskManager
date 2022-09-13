package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected StatusTask status;
    protected String startTime ;
    protected int duration ;

    public Task(String name, String description, StatusTask status,String startTime, int duration) {

        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskType getType(){
        return TaskType.TASK;
    }

    public String getTime(){
        return startTime;
    }

    public Integer getDuration(){
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getEndTime(){ // Возвращаем продолжительность выполнения задачи
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        LocalDateTime startEndTime = LocalDateTime.parse(startTime,formatter);
        startEndTime=startEndTime.plusMinutes(duration);
        return startEndTime.format(formatter);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime='" + startTime + '\'' +
                ", duration=" + duration +
                '}';
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, startTime, duration);
    }
}
