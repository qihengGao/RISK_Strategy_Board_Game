package edu.duke.ece651.risk.apiserver.security.jwt;

import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import org.junit.Test;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

public class JwtUtilsTest {

    @Test
    public void test_generateJwtToken(){
        Authentication authentication = mock(Authentication.class);
        JwtUtils jwtUtils = mock(JwtUtils.class);
        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "asdfasdf");

        doCallRealMethod().when(jwtUtils).generateJwtToken(authentication);
        doReturn(userDetails).when(authentication).getPrincipal();
        jwtUtils.generateJwtToken(authentication);

        verify(authentication).getPrincipal();
    }

    @Test
    public void test_getUserNameFromJwtToken(){
        JwtUtils jwtUtils = mock(JwtUtils.class);
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwidXNlcm5hbWUiOiJKb2huIERvZSIsImlhdCI6MTUxNjIzOTAyMn0.p5Csu2THYW5zJys2CWdbGM8GaWjpY6lOQpdLoP4D7V4";
        doCallRealMethod().when(jwtUtils).getUserNameFromJwtToken(any());
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "asdf.asdf");

//        jwtUtils.getUserNameFromJwtToken(token);
    }
}
