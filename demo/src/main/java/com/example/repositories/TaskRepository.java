package com.example.repositories;

import com.example.models.Task;
import com.example.models.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    List<Task> findByUserAndFinishedTrue(User user);

    // void updateTask(Long taskId, Task updatedTask);

    // Object getAllTasks();

    // void saveTask(Task task);

    // void deleteTask(Long taskId);

    // void getAllTasks();

 
    
}
