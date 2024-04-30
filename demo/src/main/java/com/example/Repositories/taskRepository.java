package com.example.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Task;

public interface taskRepository extends JpaRepository<Task, Long> {

}
