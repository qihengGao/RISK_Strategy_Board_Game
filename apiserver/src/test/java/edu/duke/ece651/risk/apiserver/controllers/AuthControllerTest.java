package edu.duke.ece651.risk.apiserver.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.duke.ece651.risk.apiserver.payload.request.LoginRequest;
import edu.duke.ece651.risk.apiserver.payload.request.SignupRequest;
import edu.duke.ece651.risk.apiserver.repository.RoleRepository;
import edu.duke.ece651.risk.apiserver.repository.UserRepository;
import edu.duke.ece651.risk.apiserver.security.jwt.JwtUtils;
import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
//@WithUserDetails("test")
public class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    UserRepository userRepository;
    @Mock
    RoleRepository roleRepository;
    @Mock
    PasswordEncoder encoder;
    @Mock
    JwtUtils jwtUtils;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void createMockMvc() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.authController).build();

    }

    @Test
    public void test_authenticateUser() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("test");
        request.setPassword("12345678");
        String requestStr = new ObjectMapper().writeValueAsString(request);

        Authentication authentication = mock(Authentication.class);
        doReturn(authentication).when(authenticationManager).authenticate(any());

        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
        doReturn(userDetails).when(authentication).getPrincipal();
        this.mockMvc.perform(
                post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestStr)
        ).andExpect(status().isOk());
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
