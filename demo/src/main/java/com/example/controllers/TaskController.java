package com.example.controllers;

import com.example.models.Task;
import com.example.Repositories.taskRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final taskRepository taskRepository;

    @Autowired
    public TaskController(taskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping
    public String getAllTasks(Model model) {
        model.addAttribute("tasks", taskRepository.findAll());
        return "user/tasks";
    }
//    @GetMapping("/tasks")
// public ModelAndView getTasks() {
//     List<Task> tasks = taskRepository.findAll();
//      ModelAndView modelAndView = new ModelAndView("tasks");
//     modelAndView.addObject("tasks", tasks);

//     return modelAndView;
// }
    @GetMapping("/{id}")
    public String getTaskById(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
        model.addAttribute("task", task);
        return "user/taskDetails";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("task", new Task());
        return "user/createTask";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task) {
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{id}/start")
    public String startTimer(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
    
        model.addAttribute("task", task);
        return "user/tasks";
    }
    @GetMapping("/{id}/start-focus")
    public String startFocusTimer(@PathVariable Long id, Model model, @RequestParam("duration") int duration) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));
    
        task.startTimer();
        task.setTotalDuration(task.getTotalDuration() + duration);
        taskRepository.save(task);
    
        model.addAttribute("task", task);
        return "user/tasks"; // Adjust the view name to match your template
    }

    @GetMapping("/{id}/start-break")
    public String startBreakTimer(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));

        task.startBreakTimer();
        task.setTotalDuration(task.getTotalDuration() + task.getBreakDuration());
        taskRepository.save(task);

        model.addAttribute("task", task);
        return "user/tasks";
    }
}