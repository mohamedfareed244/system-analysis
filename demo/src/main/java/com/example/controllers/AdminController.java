package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.repositories.AdminRepository;

@RestController
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    AdminRepository adminrepo;
    @GetMapping("/listusers")
    public ModelAndView GetAllUsers(){
        return new ModelAndView("ListUsers");
    }

    @GetMapping("/listadmins")
    public ModelAndView GetAllAdmins(){
        ModelAndView newmodel=new ModelAndView("ListAdmins");
        newmodel.addObject("Admins ", adminrepo.findAll());
        return newmodel;
    }
}
