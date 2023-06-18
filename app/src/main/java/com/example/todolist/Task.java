package com.example.todolist;


public class Task {

    private static String task;
    private static String notes;
    private static String minutes;
    private static String seconds;

    public Task(String task, String notes, String minutes, String seconds) {

        this.task = task;
        this.notes = notes;
        this.minutes = minutes;
        this.seconds = seconds;


    }

    public  String getTask() {
        return task;
    }

    public void setTask(String task) {
        Task.task = task;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        Task.notes = notes;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        Task.minutes = minutes;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        Task.seconds = seconds;
    }
}
