package com.example.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Admin;

public interface  AdminRepository extends JpaRepository<Admin,Integer> {
    
}
