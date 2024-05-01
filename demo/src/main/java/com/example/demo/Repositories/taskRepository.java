package com.example.demo.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Task;

@Repository
public interface taskRepository extends JpaRepository<Task, String> {

    // Optional<Task> findByName(String name);

    Optional<Task> findByname(String name);

    Optional<Task> findById(Long id);

    Optional<Task> findByid(Long id);
}
