package com.example.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.example.models.Admin;
import com.example.models.Rewards;
import com.example.models.User;
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
        String hashedPassword = BCrypt.hashpw(newone.getPassword(), BCrypt.gensalt());
        newone.setPassword(hashedPassword);
        adminrepo.save(newone);
        RedirectView Rv=new RedirectView("/Admin/listadmins");
        return Rv;
    }
    
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView mav = new ModelAndView("/admin/adminLogin");
        mav.addObject("admin", new Admin());
        return mav;
    }

    
    @PostMapping("/login")
    public RedirectView loginProgress(@RequestParam("username") String username, @RequestParam("password") String password,HttpSession session) {
        Admin admin = adminrepo.findByUserName(username);
        if (admin != null && BCrypt.checkpw(password, admin.getPassword())) {
            session.setAttribute("UserName", username); 
            return new RedirectView("/Admin/adminProfile");
        } else {
            return new RedirectView("/Admin/login");
        }
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/Admin/login");
    }

    @GetMapping("/adminProfile")
    public ModelAndView ProfileDetails(HttpSession session) {
        ModelAndView mav = new ModelAndView("/admin/adminProfile");
        String username = (String) session.getAttribute("UserName");
        if (username != null) {
            Admin admin = adminrepo.findByUserName(username);
            if (admin != null) {
                mav.addObject("userId", admin.getId());
                mav.addObject("name", admin.getName());
                mav.addObject("phone", admin.getPhone());
                mav.addObject("username", admin.getuserName());
            }
        }
        return mav;
    }
@GetMapping("/Rewards")
public ModelAndView getRewardsPage() {
    ModelAndView mav = new ModelAndView("/admin/Rewards");
    List<Rewards> rewardsList = reward_repo.findAll();
    mav.addObject("rewards", rewardsList);
    return mav;
}

@GetMapping("/addReward")
    public ModelAndView getAddRewardPage() {
        ModelAndView mav = new ModelAndView("/admin/addReward");
        mav.addObject("reward", new Rewards());
        return mav;
    }

    @PostMapping("/addReward")
    public RedirectView addReward(@ModelAttribute Rewards reward) {
        reward_repo.save(reward);
        return new RedirectView("/Admin/Rewards");
    }

    @GetMapping("/editReward")
    public ModelAndView getEditRewardPage(@RequestParam("id") Long id) {
        ModelAndView mav = new ModelAndView("/admin/editReward");
        Optional<Rewards> reward = reward_repo.findById(id);
        if (reward.isPresent()) {
            mav.addObject("reward", reward.get());
        }
        return mav;
    }

    @PostMapping("/editReward")
    public RedirectView editReward(@ModelAttribute Rewards reward) {
        reward_repo.save(reward);
        return new RedirectView("/Admin/Rewards");
    }

    @GetMapping("/deleteReward")
    public RedirectView deleteReward(@RequestParam("id") Long id) {
        reward_repo.deleteById(id);
        return new RedirectView("/Admin/Rewards");
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
