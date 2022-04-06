package edu.duke.ece651.risk.apiserver.security.jwt;

import org.junit.jupiter.api.Test;

import org.springframework.security.core.AuthenticationException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

import static org.mockito.Mockito.*;

public class AuthEntryPointJwtTest {
    @Test
    public void test_commence() throws ServletException, IOException {
        AuthEntryPointJwt jwt = mock((AuthEntryPointJwt.class));
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AuthenticationException exception = mock(AuthenticationException.class);
        ServletOutputStream stream = mock(ServletOutputStream.class);

        doCallRealMethod().when(jwt).commence(request, response, exception);
        doReturn(stream).when(response).getOutputStream();
        jwt.commence(request, response, exception);

        verify(response).setContentType(any());
        verify(response).setStatus(anyInt());
        verify(response).getOutputStream();
    }
}
