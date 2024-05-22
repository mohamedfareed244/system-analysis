package com.example.demo;

import org.mockito.Mock;

import com.example.controllers.AdminController;
import com.example.repositories.AdminRepository;

public class AdminControllerTest {
    @Mock
    private AdminRepository adminrepo;

    @Mock 
    private AdminController admincontroller;
}
