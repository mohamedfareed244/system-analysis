package com.example.controllers;

import com.example.models.Task;
import com.example.models.TimerService;
import com.example.models.User;
import com.example.repositories.TaskRepository;
import com.example.repositories.UserRepository;
import com.example.repositories.TimerServiceRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    
    @GetMapping("")
    public ModelAndView getData() {
        ModelAndView mav = new ModelAndView("landing");
        return mav;
    }
    @GetMapping("/login")
    public String index (Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "login";
    }
    // @PostMapping("/login")
    // public String login(@RequestParam String username, @RequestParam String password) {
    //     User user = userRepository.findByUsername(username);
    //     if (user != null && user.getPassword().equals(password)) {
    //         return "redirect:/timer";
    //     }
    //     return "redirect:/";
    // }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/timer";
        }
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "signup"; 
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            session.setAttribute("user", existingUser);
            return "redirect:/timer";
        }

     else{
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

      
        userRepository.save(user);

        session.setAttribute("user", user); 

     
        return "redirect:/timer";
     }
    }

    
    @GetMapping("/timer")
    public String getTimerPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        } else {
            List<Task> tasks = taskRepository.findByUser(user);
            model.addAttribute("tasks", tasks);
            TimerService timerService = new TimerService();
            model.addAttribute("timerService", timerService);
            return "timer";
        }
    }
   
    @PostMapping("/timer/start")
    public String startTimer(@RequestParam("focusDuration") int focusDuration,
                             @RequestParam("breakDuration") int breakDuration) {

        TimerService timerService = new TimerService();
        timerService.startTimer(focusDuration, breakDuration);
        return "redirect:/timer";
    }

    @PostMapping("/timer/stop")
    public String stopTimer() {
        TimerService timerService = new TimerService();
        timerService.stopTimer();
        return "redirect:/timer";
    }

    @PostMapping("/timer/reset")
    public String resetTimer() {
        TimerService timerService = new TimerService();
        timerService.resetTimer();
        return "redirect:/timer";
    }

    @PostMapping("/timer/startBreak")
    public String startBreak() {
        TimerService timerService = new TimerService();
        timerService.startBreak();
        return "redirect:/timer";
    }

    @PostMapping("/timer/addTask")
    public String addTask(@ModelAttribute Task tasks, HttpSession session) {
        User user = (User) session.getAttribute("user");
        tasks.setUser(user);

        taskRepository.save(tasks);
        return "redirect:/timer";
    }

    @GetMapping("/timer/editTask/{taskId}")
    public String showEditTaskForm(@PathVariable("taskId") Long taskId, Model model) {
        Task tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
        model.addAttribute("tasks", tasks);
        return "edit-task";
    }

    @PostMapping("/timer/editTask/{taskId}")
    public String updateTask(@PathVariable("taskId") Long taskId, @Valid @ModelAttribute("tasks") Task updatedTask,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit-task";
        } else {
            Task tasks = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
            tasks.setDescription(updatedTask.getDescription());
            taskRepository.save(tasks);
            return "redirect:/timer";
        }
    }

    @PostMapping("/timer/deleteTask/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId) {
        Task tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
        taskRepository.delete(tasks);
        return "redirect:/timer";
    }
   
    @PostMapping("/timer/finishTask/{taskId}")
    public ResponseEntity<String> finishTask(@PathVariable("taskId") Long taskId) {
        Task tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
        tasks.setFinished(true);
        taskRepository.save(tasks);
        return ResponseEntity.ok("Task finished successfully.");
    }
}