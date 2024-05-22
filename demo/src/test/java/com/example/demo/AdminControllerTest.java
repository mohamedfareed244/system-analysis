package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

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

    @Test
    void testfaillogin(){
        RedirectView rv =this.admincontroller.loginProgress("nermien@gmail.com","",session);
        assertEquals("/Admin/login", rv.getUrl());
    }

    
    @Test
    void testSuccessLogin(){
        RedirectView rv =this.admincontroller.loginProgress("nermein@gmail.com","12345678",session);
        assertEquals("/Admin/adminProfile", rv.getUrl());
        
    }
}
