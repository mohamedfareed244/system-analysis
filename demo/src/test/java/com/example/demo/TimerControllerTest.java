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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

        String result = timerController.login("testUser", "testPass", session);
        assertEquals("redirect:/user/timer", result);
        assertEquals(user, session.getAttribute("user"));
    }

    @Test
    void testSignup() {
        when(userRepository.findByUsername("newUser")).thenReturn(null);

        String result = timerController.signup("newUser", "newPass", "name", "1234567890", session);
        assertEquals("redirect:/user/timer", result);
        User user = (User) session.getAttribute("user");
        assertEquals("newUser", user.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    
}
