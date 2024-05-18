package com.example.controllers;

import java.util.ArrayList;
import java.util.List;

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
import com.example.models.Rewards;
import com.example.repositories.AdminRepository;
import com.example.repositories.RewardsRepository;
import com.example.repositories.UserRepository;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/Admin")
public class AdminController {
    @Autowired
    AdminRepository adminrepo;
    @Autowired
    UserRepository user_repo;

    @Autowired
    RewardsRepository reward_repo;

    @GetMapping("")
    public ModelAndView Getindex(){
           ModelAndView newmodel=new ModelAndView("/admin/adminProfile");
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

@GetMapping("/adminProfile")
public ModelAndView ProfileDetails(HttpSession session) {
    ModelAndView mav = new ModelAndView("/admin/adminProfile");
    String username = (String) session.getAttribute("Username");
    if (username != null) {
        Admin admin = adminrepo.findByUserName(username);
        if (admin!= null) {
            mav.addObject("userId", admin.getId());
            mav.addObject("name", admin.getName());
            mav.addObject("phone", admin.getPhone());
            mav.addObject("username", admin.getuserName());
          
           
        }
    }
    return mav;
}

@GetMapping("/logout")
public RedirectView logout(HttpSession session) {
   
    session.invalidate();
   
    return new RedirectView("/login");
}
@GetMapping("/Rewards")
public ModelAndView getRewardsPage() {
     
    ModelAndView mav = new ModelAndView("/admin/Rewards");

    List<Rewards> rewardsList = reward_repo.findAll();
    if (rewardsList.isEmpty()) {
        rewardsList.add(new Rewards(1L, "Tasks More Than Or Equal Ten", "10%"));
        rewardsList.add(new Rewards(2L, "Tasks More Than Or Equal Five", "5%"));
        rewardsList.add(new Rewards(3L, "Tasks Less Than Five", "0%"));
    }

    mav.addObject("rewards", rewardsList);
    return mav;

}

private double calculateDiscountPercentage(int totalFinishedTasks) {

    if (totalFinishedTasks >= 10) {
        return 0.1;
    } else if (totalFinishedTasks >= 5) {
        return 0.05;
    } else {
        return 0.0;
    }
}

}
