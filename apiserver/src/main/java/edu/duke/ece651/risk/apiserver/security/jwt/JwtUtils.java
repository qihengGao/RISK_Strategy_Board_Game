package edu.duke.ece651.risk.apiserver.security.jwt;


import edu.duke.ece651.risk.apiserver.security.services.UserDetailsImpl;
import edu.duke.ece651.risk.apiserver.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Value("${bezkoder.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    /**
     * generate a JWT token
     * @param authentication
     * @return generated JWT token
     */
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    /**
     * get username from token
     * @param token
     * @return extracted username
     */
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * validate a JWT
     * @param authToken
     * @return true if validated, false otherwise
     */
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (Exception e) {
            logger.error("Invalid JWT: {}", e.getMessage());
        }
        return false;
    }

    public UserDetails getUserFromToken(String token) {
        token = token.substring(7);
        String username = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
        System.out.println(username);
        return userDetailsService.loadUserByUsername(username);
    }


    public Authentication getAuthenticationFromToken(String token) {
        if (token != null) {
            UserDetails user = getUserFromToken(token);

            if (user != null) {
                System.out.println(user);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(),user.getAuthorities());
                authentication.setDetails(user);
                //authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //SecurityContextHolder.getContext().setAuthentication(authentication);
                return authentication;

                //return new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
            }
        }

        return null;
    }



}