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

    public Task findTaskById(int taskId) {
        Task task = new Task();
        // SQL Query + Connection Try Catch
        return task;
    }

    // Sort Metoder

    public void sortByHours() {
        /*
        Skal sortere tasks efter timer
         */
    }

    public void sortByDepartment() {
        /*
        Skal sortere tasks efter afdeling
         */
    }

    public void sortByStatus() {
        /*
        Skal sortere tasks efter status
         */
    }

    public void sortByFlag() {
        /*
        Skal sortere tasks efter flag
         */
    }


    public int timeOverview() {
        /*
        Skal vise resultatet af totale timer der er
        brugt og er til overs i tasks
         */

        // int totalHours = ???

        // return totalHours;
        return  0;
    }

    public int priceOverview() {
        /*
        Skal vise resultatet af totale timer der er
        brugt og er til overs i tasks, ganget med
        gennesnitlig timeløn (payRate)
         */

        // int payRate = 300;
        // int totalHours = ???

        // int price = totalHours * payRate;

        // return price;
        return 0;
    }

}
