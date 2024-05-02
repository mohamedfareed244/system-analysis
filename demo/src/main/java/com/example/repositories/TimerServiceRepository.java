package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.TimerService;

public interface TimerServiceRepository extends JpaRepository<TimerService, Long> {

 
}
