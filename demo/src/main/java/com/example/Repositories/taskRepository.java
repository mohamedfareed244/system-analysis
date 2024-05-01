package com.example.Repositories;

import com.example.models.Task;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface taskRepository extends JpaRepository<Task, Long> {

    // Optional<Task> findByName(String name);

    Optional<Task> findByname(String name);

    Optional<Task> findById(Long id);

    Optional<Task> findByid(Long id);
}
