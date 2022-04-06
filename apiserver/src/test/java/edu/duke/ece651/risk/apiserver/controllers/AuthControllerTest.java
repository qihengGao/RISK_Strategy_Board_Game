package edu.duke.ece651.risk.apiserver.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece651.risk.apiserver.controllers.AuthController;
import edu.duke.ece651.risk.apiserver.controllers.GameController;
import edu.duke.ece651.risk.apiserver.payload.request.LoginRequest;
import edu.duke.ece651.risk.apiserver.payload.request.SignupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@WithUserDetails("test")
public class AuthControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void createMockMvc(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.authController).build();
    }

    @Test
    public void test_authenticateUser() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("test");
        request.setPassword("12345678");
        String requestStr = new ObjectMapper().writeValueAsString(request);

//        this.mockMvc.perform(
//            post("/api/auth/signin")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(requestStr)
//        ).andExpect(status().isOk());
    }

    @Test
    public void test_registerUser() throws Exception {
        HashSet<String> roles = new HashSet<>();
        roles.add("admin");

        SignupRequest request = new SignupRequest();
        request.setUsername("test");
        request.setPassword("12345678");
        request.setEmail("abc@gmail.com");
        request.setRole(roles);
        String requestStr = new ObjectMapper().writeValueAsString(request);

//        this.mockMvc.perform(
//            post("/api/auth/signup")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(requestStr)
//        ).andExpect(status().isOk());
    }
}
