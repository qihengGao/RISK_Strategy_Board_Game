package edu.duke.ece651.risk.apiserver.security.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class AuthTokenFilterTest {

    @Test
    public void test_doFilterInternal() throws ServletException, IOException {
        AuthTokenFilter filter = mock(AuthTokenFilter.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        doCallRealMethod().when(filter).doFilterInternal(request, response, chain);
        doReturn("asdfasdf").when(filter).parseJwt(request);
        filter.doFilterInternal(request, response, chain);

        verify(chain).doFilter(any(), any());
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
