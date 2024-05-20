package com.example.controllers;

import java.text.DecimalFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
@RequestMapping("/user")
public class TimerController {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TimerServiceRepository timerServiceRepository;

    public TimerController(TaskRepository taskRepository, UserRepository userRepository,
            TimerServiceRepository timerServiceRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.timerServiceRepository = timerServiceRepository;
    }

    @GetMapping("Home")
    public ModelAndView getData() {
        ModelAndView mav = new ModelAndView("landing");
        return mav;
    }

    @GetMapping("/login")
    public String index(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "login";
    }

    // @PostMapping("/login")
    // public String login(@RequestParam String username, @RequestParam String
    // password) {
    // User user = userRepository.findByUsername(username);
    // if (user != null && user.getPassword().equals(password)) {
    // return "redirect:/timer";
    // }
    // return "redirect:/";
    // }
    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/user/timer";
        }
        return "redirect:/user/login";
    }

    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "/signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password, @RequestParam String name,
            @RequestParam String phonenumber, HttpSession session) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            session.setAttribute("user", existingUser);
            return "redirect:/user/signup";
        }

        else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setName(name);
            user.setPhonenumber(phonenumber);

            userRepository.save(user);

            session.setAttribute("user", user);

            return "redirect:/user/timer";
        }
    }

    @GetMapping("/profile")
    public ModelAndView viewProfile(HttpSession session) {
        ModelAndView mav = new ModelAndView("profile.html");
        User user = (User) session.getAttribute("user");

          String name = (String) session.getAttribute("name");
        String username = (String) session.getAttribute("username");
        String phonenumber = (String) session.getAttribute("phonenumber");

        mav.addObject("name", user.getName());
        mav.addObject("username", user.getUsername());
        mav.addObject("phonenumber", user.getPhonenumber());

        return mav;
    }

    @GetMapping("/{id}/edituser")
    public String edituser(@PathVariable("id") Long id, Model model) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        model.addAttribute("user", user);
        return "/user/updateProfile";
    }

    @PostMapping("/{id}/edituser")
    public String updateuser(@Valid @PathVariable("id") Long id, @ModelAttribute("User") User updateduser,
            BindingResult bindingResult) {
                User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        user.setUsername(updateduser.getUsername());
        if (bindingResult.hasErrors()) {
            return "redirect:/user/updateProfile";
        } else {
            userRepository.save(user);
            return "redirect:/user/Profile";
        }
    }

    // @GetMapping("/finished-tasks")
    // public ModelAndView getFinishedTasks(HttpSession session) {
    //     ModelAndView mav = new ModelAndView("finished-tasks.html");
    //     User user = (User) session.getAttribute("user");

    //     if (user == null) {
    //         mav.setViewName("redirect:/user/login");
    //         return mav;
    //     } else {
    //         List<Task> finishedTasks = taskRepository.findByUserAndFinishedTrue(user);
    //         mav.addObject("finishedTasks", finishedTasks);
    //         return mav;
    //     }
    // }

    @GetMapping("/user-report")
    public ModelAndView generateUserReport(HttpSession session) {
        ModelAndView mav = new ModelAndView("user-report.html");
        User user = (User) session.getAttribute("user");

        if (user == null) {
            mav.setViewName("redirect:/user/login");
            return mav;
        } else {
            List<Task> finishedTasks = taskRepository.findByUserAndFinishedTrue(user);
            mav.addObject("finishedTasks", finishedTasks);

            int totalFinishedTasks = finishedTasks.size();
            double discountPercentage = calculateDiscountPercentage(totalFinishedTasks);

            BigDecimal discountPercentageBigDecimal = BigDecimal.valueOf(discountPercentage * 100);

            discountPercentageBigDecimal = discountPercentageBigDecimal.setScale(2, RoundingMode.HALF_UP);

            mav.addObject("discountPercentage", discountPercentageBigDecimal);

            return mav;
        }
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

    @GetMapping("/timer")
    public String getTimerPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        } else {
            List<Task> tasks = taskRepository.findByUser(user);
            model.addAttribute("tasks", tasks);
            model.addAttribute("user", user);
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
        return "redirect:/user/timer";
    }

    @PostMapping("/timer/stop")
    public String stopTimer() {
        TimerService timerService = new TimerService();
        timerService.stopTimer();
        return "redirect:/user/timer";
    }

    @PostMapping("/timer/reset")
    public String resetTimer() {
        TimerService timerService = new TimerService();
        timerService.resetTimer();
        return "redirect:/user/timer";
    }

    @PostMapping("/timer/startBreak")
    public String startBreak() {
        TimerService timerService = new TimerService();
        timerService.startBreak();
        return "redirect:/user/timer";

    }

    @PostMapping("/timer/addTask")
    public String addTask(@ModelAttribute Task tasks, HttpSession session) {
        User user = (User) session.getAttribute("user");
        tasks.setUser(user);

        taskRepository.save(tasks);
        return "redirect:/user/timer";

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
            return "redirect:/user/timer";

        }
    }

    @PostMapping("/timer/deleteTask/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId) {
        Task tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
        taskRepository.delete(tasks);
        return "redirect:/user/timer";
    }

    @PostMapping("/timer/finishTask/{taskId}")
    public String finishTask(@PathVariable("taskId") Long taskId) {
        Task tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
        tasks.setFinished(true);
        taskRepository.save(tasks);
        return "redirect:/user/timer";
    }

    // @PostMapping("/timer/editTask/{taskId}")
    // public ResponseEntity<String> updateTask(@PathVariable("taskId") Long taskId,
    // @Valid @ModelAttribute("tasks") Task updatedTask,
    // BindingResult bindingResult) {
    // if (bindingResult.hasErrors()) {
    // return ResponseEntity.badRequest().body("Invalid task data.");
    // } else {
    // Task tasks = taskRepository.findById(taskId)
    // .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " +
    // taskId));
    // tasks.setDescription(updatedTask.getDescription());
    // taskRepository.save(tasks);
    // return ResponseEntity.ok("Task updated successfully.");
    // }
    // }

    // @PostMapping("/timer/deleteTask/{taskId}")
    // public ResponseEntity<String> deleteTask(@PathVariable("taskId") Long taskId)
    // {
    // Task tasks = taskRepository.findById(taskId)
    // .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " +
    // taskId));
    // taskRepository.delete(tasks);
    // return ResponseEntity.ok("Task deleted successfully.");
    // }

      
   
}