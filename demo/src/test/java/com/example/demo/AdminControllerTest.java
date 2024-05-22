package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;

import com.example.controllers.AdminController;
import com.example.repositories.AdminRepository;

public class AdminControllerTest {
    @Mock
    private AdminRepository adminrepo;

    @InjectMocks
    private AdminController admincontroller;

    private MockHttpSession session;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        session = new MockHttpSession();
    }

    @Test
    void testAdminProfile(){
        ModelAndView mav =this.admincontroller.ProfileDetails(session);
        assertEquals("/admin/adminProfile", mav.getViewName());
    }
}
