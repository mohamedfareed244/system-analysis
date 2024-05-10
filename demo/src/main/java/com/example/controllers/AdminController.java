package com.example.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/Admin")
public class AdminController {
    @GetMapping("/listusers")
    public ModelAndView GetAllUsers(){
        return new ModelAndView("ListUsers");
    }

    @GetMapping("/listadmins")
    public ModelAndView GetAllAdmins(){
        return new ModelAndView("ListAdmins");
    }
}
