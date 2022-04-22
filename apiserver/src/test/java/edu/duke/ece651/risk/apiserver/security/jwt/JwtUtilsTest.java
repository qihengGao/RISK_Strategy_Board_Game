package edu.duke.ece651.risk.apiserver.security.jwt;

//import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Spy;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.Authentication;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Date;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.*;
//@RunWith(MockitoJUnitRunner.class)
//public class JwtUtilsTest {
//
////    @Mock
////    private String jwtSecret = "asdfasdf";
//
//
//
//
//
//    @Test
//    @Disabled
//    public void test_generateJwtToken(){
//        Authentication authentication = mock(Authentication.class);
//        JwtUtils jwtUtils = mock(JwtUtils.class);
//        UserDetailsImpl userDetails = mock(UserDetailsImpl.class);
//
//        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "asdfasdf");
//
//        doCallRealMethod().when(jwtUtils).generateJwtToken(authentication);
//        doReturn(userDetails).when(authentication).getPrincipal();
//        jwtUtils.generateJwtToken(authentication);
//
//        verify(authentication).getPrincipal();
//    }
//
//    @Test
//    public void test_getUserNameFromJwtToken(){
//        String token = Jwts.builder()
//                .setSubject(("test"))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + 860000))
//                .signWith(SignatureAlgorithm.HS512, "ece651riskSecretKey")
//                .compact();
//        JwtUtils jwtUtils =  new JwtUtils();
//
//        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "ece651riskSecretKey");
//
//
//       String res = jwtUtils.getUserNameFromJwtToken(token);
//        assertEquals("test",res);
//    }
//
//
//    @Test
//    public void test_validateJwtToken(){
//        String token = Jwts.builder()
//                .setSubject(("test"))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + 86000))
//                .signWith(SignatureAlgorithm.HS512, "ece651riskSecretKey")
//                .compact();
//        JwtUtils jwtUtils =  new JwtUtils();
//
//        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "ece651riskSecretKey");
//
//
//        Boolean aBoolean = jwtUtils.validateJwtToken(token);
//        assertEquals(true,aBoolean);
//
//
//        token = Jwts.builder()
//                .setSubject(("test"))
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + 0))
//                .signWith(SignatureAlgorithm.HS512, "ece651riskSecretKey")
//                .compact();
//
//        aBoolean = jwtUtils.validateJwtToken(token);
//        assertEquals(false,aBoolean);
//
//
//    }
//
//}
