package com.example.controllers;

import com.example.models.Task;
import com.example.models.TimerService;
import com.example.models.User;
import com.example.repositories.TaskRepository;
import com.example.repositories.UserRepository;

import jakarta.validation.Valid;

import com.example.repositories.TimerServiceRepository;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
public class TimerController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TimerServiceRepository timerServiceRepository;

    public TimerController(TaskRepository taskRepository, UserRepository userRepository, TimerServiceRepository timerServiceRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.timerServiceRepository = timerServiceRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("task", taskRepository.findAll());
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "redirect:/timer";
        }
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup"; // Return the signup form page
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            // User with the same username already exists
            return "redirect:/timer";
        }

        // Create a new user
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        // Save the user to the database
        userRepository.save(user);

        // Redirect to the timer page
        return "redirect:/timer";
    }

    @GetMapping("/timer")
    public String getTimerPage(Model model) {
        TimerService timerService= new TimerService();
        model.addAttribute("timerService", timerService);
        model.addAttribute("tasks", taskRepository.findAll());
        return "timer";
    }

    @PostMapping("/timer/start")
    public String startTimer(@RequestParam("focusDuration") int focusDuration,
                             @RequestParam("breakDuration") int breakDuration) {
                          
                                TimerService timerService= new TimerService();
                                timerService.startTimer(timerService.focusDuration, timerService.breakDuration);
        return "redirect:/timer";
    }

    @PostMapping("/timer/stop")
    public String stopTimer() {
        TimerService timerService= new TimerService();
        timerService.stopTimer();
        return "redirect:/timer";
    }

    @PostMapping("/timer/reset")
    public String resetTimer() {
        TimerService timerService= new TimerService();
        timerService.resetTimer();
        return "redirect:/timer";
    }

    @PostMapping("/timer/startBreak")
    public String startBreak() {
        TimerService timerService= new TimerService();
        timerService.startBreak();
        return "redirect:/timer";
    }

    @PostMapping("/timer/addTask")
    public String addTask(@ModelAttribute Task task) {
        
        taskRepository.save(task);
        return "redirect:/timer";
    }
    // @PostMapping("/timer/editTask/{taskId}")
    // public String editTask(@PathVariable("taskId") Long taskId, @ModelAttribute Task updatedTask) {
    //     taskRepository.updateTask(taskId, updatedTask);
    //     return "redirect:/timer";
    // }
@GetMapping("/timer/{id}/editTask")
    public String showEditAdminForm(@PathVariable("id") Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin ID: " + id));
        model.addAttribute("task", task);
        return "redirect:/timer";
    }

    @PostMapping("/timer/{id}/editTask")
    public String updateAdmin(@Valid @PathVariable("id") Long id, @ModelAttribute("task") Task updatedTask,
            BindingResult bindingResult) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid admin ID: " + id));
        task.setDescription(updatedTask.getDescription());
        if (bindingResult.hasErrors()) {
            return "redirect:/timer";
        } else {
            taskRepository.save(task);
            return "redirect:/timer";
        }
    }

    @PostMapping("/timer/deleteTask/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId) {
        taskRepository.deleteById(taskId);
        return "redirect:/timer";
    }
}