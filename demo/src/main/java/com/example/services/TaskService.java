package com.example.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.models.Task;
import com.example.repositories.TaskRepository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    public void deleteFinishedTasks() {
        LocalDate today = LocalDate.now();
        List<Task> finishedTasks = taskRepository.findByFinishedTrue();
        
        for (Task task : finishedTasks) {
            Date finishDate = (Date) task.getFinishDate();

            if (finishDate != null) {
                // Convert Date to LocalDate
                LocalDate taskFinishDate = finishDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                
                if (taskFinishDate.isBefore(today)) {
                    // Delete task
                    taskRepository.delete(task);
                }
            } else {
                // Handle case where finishDate is null
                System.err.println("Finish date is null for task: " + task.getId());
            }
        }
    }
}
