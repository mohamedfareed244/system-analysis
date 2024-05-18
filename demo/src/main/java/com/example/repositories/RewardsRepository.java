package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Rewards;

public interface RewardsRepository extends JpaRepository<Rewards,Long> {
    
    
}
