package com.example.demo;

import com.example.controllers.TimerController;
import com.example.models.Task;
import com.example.models.User;
import com.example.repositories.TaskRepository;
import com.example.repositories.UserRepository;
import com.example.repositories.TimerServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class TimerControllerTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TimerServiceRepository timerServiceRepository;

    @InjectMocks
    private TimerController timerController;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
    }

    @Test
    void testGetData() {
        ModelAndView mav = timerController.getData();
        assertEquals("landing", mav.getViewName());
    }

    @Test
    void testLogin() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPass");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        String result = timerController.login("testUser", "testPass", session, null);
        assertEquals("redirect:/user/timer", result);
        assertEquals(user, session.getAttribute("user"));
    }

    @Test
    void testLoginInvalidPassword() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("wrongPass");
        when(userRepository.findByUsername("testUser")).thenReturn(user);

        String result = timerController.login("testUser", "testPass", session, null);
        assertEquals("redirect:/user/login", result);
        assertNull(session.getAttribute("user"));
    }

    @Test
    void testSignup() {
        when(userRepository.findByUsername("newUser")).thenReturn(null);

        String result = timerController.signup("newUser", "newPass", "name", "phonenumber", session, null);
        assertEquals("redirect:/user/timer", result);
        User user = (User) session.getAttribute("user");
        assertEquals("newUser", user.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignupExistingUser() {
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        when(userRepository.findByUsername("existingUser")).thenReturn(existingUser);

        String result = timerController.signup("existingUser", "newPass", "name", "phonenumber", session, null);
        assertEquals("redirect:/user/signup", result);
        assertEquals(existingUser, session.getAttribute("user"));
        verify(userRepository, times(0)).save(any(User.class));
    }

    @Test
    void testGetTimerPage() {
        User user = new User();
        session.setAttribute("user", user);
        when(taskRepository.findByUser(user)).thenReturn(Collections.emptyList());

        String result = timerController.getTimerPage(model, session);
        assertEquals("timer", result);
        verify(model, times(1)).addAttribute(eq("tasks"), anyList());
        verify(model, times(1)).addAttribute(eq("user"), eq(user));
    }

    @Test
    void testGetTimerPageNoUserInSession() {
        String result = timerController.getTimerPage(model, session);
        assertEquals("redirect:/user/login", result);
    }

    @Test
    void testAddTask() {
        User user = new User();
        session.setAttribute("user", user);
        Task task = new Task();
        task.setDescription("Test Task");

        String result = timerController.addTask(task, session);
        assertEquals("redirect:/user/timer", result);
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    void testAddTaskNoUserInSession() {
        Task task = new Task();
        task.setDescription("Test Task");

        String result = timerController.addTask(task, session);
        assertEquals("redirect:/user/login", result);
        verify(taskRepository, times(0)).save(task);
    }

    @Test
    void testUpdateTask() {
        Task task = new Task();
        task.setId(1L);
        task.setDescription("Old Description");
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task updatedTask = new Task();
        updatedTask.setDescription("New Description");

        String result = timerController.updateTask(1L, updatedTask, bindingResult);
        assertEquals("redirect:/user/timer", result);
        verify(taskRepository, times(1)).save(task);
        assertEquals("New Description", task.getDescription());
    }

    @Test
    void testUpdateTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Task updatedTask = new Task();
        updatedTask.setDescription("New Description");

        String result = timerController.updateTask(1L, updatedTask, bindingResult);
        assertEquals("redirect:/user/timer", result);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testUpdateTaskWithErrors() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(bindingResult.hasErrors()).thenReturn(true);

        Task updatedTask = new Task();
        updatedTask.setDescription("New Description");

        String result = timerController.updateTask(1L, updatedTask, bindingResult);
        assertEquals("edit-task", result);
        verify(taskRepository, times(0)).save(any(Task.class));
    }

    @Test
    void testDeleteTask() {
        Task task = new Task();
        task.setId(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        String result = timerController.deleteTask(1L);
        assertEquals("redirect:/user/timer", result);
        verify(taskRepository, times(1)).delete(task);
    }

    @Test
    void testDeleteTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        String result = timerController.deleteTask(1L);
        assertEquals("redirect:/user/timer", result);
        verify(taskRepository, times(0)).delete(any(Task.class));
    }

    @Test
    void testFinishTask() {
        Task task = new Task();
        task.setId(1L);
        task.setFinished(false);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        String result = timerController.finishTask(1L);
        assertEquals("redirect:/user/timer", result);
        verify(taskRepository, times(1)).save(task);
        assertEquals(true, task.getFinished());
    }

    @Test
    void testFinishTaskNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        String result = timerController.finishTask(1L);
        assertEquals("redirect:/user/timer", result);
        verify(taskRepository, times(0)).save(any(Task.class));
    }
}
