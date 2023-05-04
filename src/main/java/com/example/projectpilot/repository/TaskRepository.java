package com.example.projectpilot.repository;
import com.example.projectpilot.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskRepository {

    public List<Task> getAllTasks() {
        List<Task> allTasksList = new ArrayList<>();
        // SQL Query + Connection Try Catch
        return allTasksList;
    }

    public void addTask(Task task) {
        // SQL Query + Connection Try Catch
    }

    public void updateTask(Task task) {
        // SQL Query + Connection Try Catch
    }

    public void deleteTask(int taskId) {
        // SQL Query + Connection Try Catch
    }

    public Task getTaskById(int taskId) {
        Task task = new Task();
        // SQL Query + Connection Try Catch
        return task;
    }




}
