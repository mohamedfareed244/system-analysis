package com.example.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.view.RedirectView;

import com.example.models.Admin;
import com.example.repositories.AdminRepository;
import com.example.repositories.UserRepository;

import ch.qos.logback.core.model.Model;

@RestController
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    AdminRepository adminrepo;
    @Autowired
    UserRepository user_repo;

    @GetMapping("")
    public ModelAndView Getindex(){
           ModelAndView newmodel=new ModelAndView("/admin/sidebar");
        return newmodel;
    }
    @GetMapping("/listusers")
    public ModelAndView GetAllUsers(){
           ModelAndView newmodel=new ModelAndView("/admin/ListUsers");
        newmodel.addObject("users", user_repo.findAll());
        return newmodel;
    }

    @GetMapping("/listadmins")
    public ModelAndView GetAllAdmins(){
        ModelAndView newmodel=new ModelAndView("/admin/ListAdmins");
        newmodel.addObject("admins", adminrepo.findAll());
      
        return newmodel;
    }
    @GetMapping("/addadmin")
    public ModelAndView GetAddAminPage(){
        Admin newadmin=new Admin();
        ModelAndView mav=new ModelAndView("/admin/AddAdmin");
        mav.addObject("NewUser", newadmin);
        return mav;
    }

    @PostMapping ("/addadmin")
    public RedirectView AddAdmin(@ModelAttribute("NewUser") Admin newone){
   adminrepo.save(newone);
        RedirectView Rv=new RedirectView("/Admin/listadmins");
        return Rv;
    }
}
