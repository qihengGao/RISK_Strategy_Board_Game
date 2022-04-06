package edu.duke.ece651.risk.apiserver.payload.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;

    /**
     * default constructor
     * @param accessToken
     * @param id
     * @param username
     * @param email
     * @param roles
     */
    public JwtResponse(String accessToken, Long id, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    /**
     * get access token
     * @return access token
     */
    public String getAccessToken() {
        return token;
    }

    /**
     * set access token
     * @param accessToken
     */
    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    /**
     * get token type
     * @return token type
     */
    public String getTokenType() {
        return type;
    }

    /**
     * set token type
     * @param tokenType
     */
    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    /**
     * get id
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * set id
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * get email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * set email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get username
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * set username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get roles
     * @return list of roles
     */
    public List<String> getRoles() {
        return roles;
    }
}