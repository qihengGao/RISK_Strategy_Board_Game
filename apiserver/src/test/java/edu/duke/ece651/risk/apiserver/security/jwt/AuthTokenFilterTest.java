package edu.duke.ece651.risk.apiserver.security.jwt;




import edu.duke.ece651.risk.apiserver.security.services.UserDetailsServiceImpl;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.*;

import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;


public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;
    @Mock
    private UserDetailsServiceImpl userDetailsService;


    @Spy
    @InjectMocks
    private AuthTokenFilter authTokenFilter;


    @BeforeEach
    private void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_doFilterInternal() throws ServletException, IOException {


        //AuthTokenFilter filter = mock(AuthTokenFilter.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        UserDetails userDetails = mock(UserDetails.class);

        doReturn(true).when(jwtUtils).validateJwtToken(any());
        
        doReturn("asdfasdf").when(authTokenFilter).parseJwt(any());
        doReturn(userDetails).when(userDetailsService).loadUserByUsername(any());

        authTokenFilter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(any(), any());

        doThrow(new UsernameNotFoundException("")).when(userDetailsService).loadUserByUsername(any());
        authTokenFilter.doFilterInternal(request, response, chain);
    }

    @Test
    public void test_parseJwt(){
        AuthTokenFilter filter = mock(AuthTokenFilter.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        String headerAuth = "Bearer asdfasdfasfd";

        doCallRealMethod().when(filter).parseJwt(request);
        doReturn(headerAuth).when(request).getHeader(any());

        String jwt = filter.parseJwt(request);
        assertNotNull(jwt);

        doReturn("Asdfasdf").when(request).getHeader(any());
        jwt = filter.parseJwt(request);
        assertNull(jwt);
    }
}
