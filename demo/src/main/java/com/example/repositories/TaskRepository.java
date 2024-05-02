package com.example.repositories;

import com.example.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    // void updateTask(Long taskId, Task updatedTask);

    // Object getAllTasks();

    // void saveTask(Task task);

    // void deleteTask(Long taskId);

    // void getAllTasks();

 
    
}
