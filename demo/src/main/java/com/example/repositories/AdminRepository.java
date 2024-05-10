package com.example.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.models.Admin;

public interface  AdminRepository extends JpaRepository<Admin,Integer> {
    List<Admin> findAll();
}
