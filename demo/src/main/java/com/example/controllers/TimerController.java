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

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.mock.web.MockHttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(username);
        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            session.setAttribute("user", user);
            return "redirect:/user/timer";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password.");
        return "redirect:/user/login";
    }
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "/signup";
    }

    @PostMapping("/signup")
    public String signup(@RequestParam String username, @RequestParam String password, @RequestParam String name,
            @RequestParam String phonenumber, HttpSession session, RedirectAttributes redirectAttributes) {
        User existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email already taken.");
            return "redirect:/user/signup";
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt(12)));
            user.setName(name);
            user.setPhonenumber(phonenumber);
    
            userRepository.save(user);
    
            session.setAttribute("user", user);
    
            return "redirect:/user/timer";
        }
    }


    @GetMapping("profile/{id}")
public ModelAndView getProfile(@PathVariable("id") Long userId) {
    ModelAndView mav = new ModelAndView("profile");
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
        mav.addObject("user", user);
        return mav;
    }
    ModelAndView errorMav = new ModelAndView("error");
    errorMav.addObject("errorMessage", "User not found");
    return errorMav;
}

@GetMapping("edit-profile/{id}")
public ModelAndView editProfile(@PathVariable("id") Long userId) {
    ModelAndView mav = new ModelAndView("editProfile");
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
        mav.addObject("user", user);
        return mav;
    }
    ModelAndView errorMav = new ModelAndView("error");
    errorMav.addObject("errorMessage", "User not found");
    return errorMav;
}

@PostMapping("edit-profile/{id}")
public RedirectView saveProfile(@PathVariable("id") Long userId, @ModelAttribute User updatedUser, HttpSession session) {
    User user = userRepository.findById(userId).orElse(null);
    if (user != null) {
        user.setName(updatedUser.getName());
        user.setUsername(updatedUser.getUsername());
        user.setPhonenumber(updatedUser.getPhonenumber());
        
        // Update password only if a new password is provided
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            String encodedPassword = BCrypt.hashpw(updatedUser.getPassword(), BCrypt.gensalt(12));
            user.setPassword(encodedPassword);
        }

        userRepository.save(user);
        session.setAttribute("user", user); // Update session user
        return new RedirectView("/user/profile/" + userId);
    }
    return new RedirectView("/user/error");
}


    @GetMapping("/finished-tasks")
    public ModelAndView getFinishedTasks(HttpSession session) {
        ModelAndView mav = new ModelAndView("finished-tasks.html");
        User user = (User) session.getAttribute("user");

        if (user == null) {
            mav.setViewName("redirect:/user/login");
            return mav;
        } else {
            List<Task> finishedTasks = taskRepository.findByUserAndFinishedTrue(user);
            mav.addObject("finishedTasks", finishedTasks);
            return mav;
        }
    }

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

        // Add welcome message with the user's name
        mav.addObject("welcomeMessage", "Welcome to User Report, " + user.getName());

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

    // @GetMapping("/timer")
    // public String getTimerPage(Model model, HttpSession session) {
    //     User user = (User) session.getAttribute("user");
    //     if (user == null) {
    //         return "redirect:/user/login";
    //     } else {
    //         List<Task> tasks = taskRepository.findByUser(user);
    //         model.addAttribute("tasks", tasks);
    //         model.addAttribute("user", user);
    //         TimerService timerService = new TimerService();
    //         model.addAttribute("timerService", timerService);
    //         return "timer";
    //     }
    // }
    @GetMapping("/timer")
    public String getTimerPage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        } else {
            model.addAttribute("tasks", taskRepository.findByUser(user));
            model.addAttribute("user", user);
            model.addAttribute("focusDuration", user.getFocusDuration());
            model.addAttribute("breakDuration", user.getBreakDuration());
            return "timer";
        }
    }

    @PostMapping("/timer/updateDurations")
    @ResponseBody
    public ResponseEntity<String> updateDurations(@RequestParam int focusDuration, @RequestParam int breakDuration, HttpSession session) {
        User user = (User) session.getAttribute("user"); 
        if (user != null) {
            user.setFocusDuration(focusDuration);
            user.setBreakDuration(breakDuration);
            userRepository.save(user);
            return ResponseEntity.ok("Durations updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not logged in.");
        }
    }
    
    // @PostMapping("/timer/start")
    // public String startTimer(@RequestParam("focusDuration") int focusDuration,
    //         @RequestParam("breakDuration") int breakDuration) {

    //     TimerService timerService = new TimerService();
    //     timerService.startTimer(focusDuration, breakDuration);
    //     return "redirect:/user/timer";
    // }

    // @PostMapping("/timer/stop")
    // public String stopTimer() {
    //     TimerService timerService = new TimerService();
    //     timerService.stopTimer();
    //     return "redirect:/user/timer";
    // }

    // @PostMapping("/timer/reset")
    // public String resetTimer() {
    //     TimerService timerService = new TimerService();
    //     timerService.resetTimer();
    //     return "redirect:/user/timer";
    // }

    // @PostMapping("/timer/startBreak")
    // public String startBreak() {
    //     TimerService timerService = new TimerService();
    //     timerService.startBreak();
    //     return "redirect:/user/timer";

    // }

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

    //for nav
    @GetMapping("/timer/editTasks/{taskId}")
    public String showEditTaskForms(@PathVariable("taskId") Long taskId, Model model) {
        Task tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
        model.addAttribute("tasks", tasks);
        return "edit-task";
    }

    @PostMapping("/timer/editTasks/{taskId}")
    public String updateTasks(@PathVariable("taskId") Long taskId, @Valid @ModelAttribute("tasks") Task updatedTask,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "finished-tasks";
        } else {
            Task tasks = taskRepository.findById(taskId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
            tasks.setDescription(updatedTask.getDescription());
            taskRepository.save(tasks);
            return "redirect:/user/finished-tasks";

        }
    }

    @PostMapping("/timer/deleteTasks/{taskId}")
    public String deleteTasks(@PathVariable("taskId") Long taskId) {
        Task tasks = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + taskId));
        taskRepository.delete(tasks);
        return "redirect:/user/finished-tasks";
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpSession session) {
        session.invalidate();
        return new RedirectView("/user/Home");
    }

  
}