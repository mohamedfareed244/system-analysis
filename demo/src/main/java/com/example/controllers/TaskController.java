package com.example.controllers;

import com.example.models.Task;
import com.example.Repositories.taskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "user/startTimer";
    }

    @GetMapping("/{id}/start-focus")
    public String startFocusTimer(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));

        task.startTimer();
        task.setTotalDuration(task.getTotalDuration() + task.getFocusDuration());
        taskRepository.save(task);

        model.addAttribute("task", task);
        return "user/startBreakTimer";
    }

    @GetMapping("/{id}/start-break")
    public String startBreakTimer(@PathVariable Long id, Model model) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid task ID: " + id));

        task.startBreakTimer();
        task.setTotalDuration(task.getTotalDuration() + task.getBreakDuration());
        taskRepository.save(task);

        model.addAttribute("task", task);
        return "taskDetails";
    }
}