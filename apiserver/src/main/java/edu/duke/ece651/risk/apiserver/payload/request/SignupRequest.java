package edu.duke.ece651.risk.apiserver.payload.request;

import javax.validation.constraints.*;
import java.util.Set;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> role;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

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
     * get password
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * get roles
     * @return roles
     */
    public Set<String> getRole() {
        return this.role;
    }

    /**
     * set roles
     * @param role
     */
    public void setRole(Set<String> role) {
        this.role = role;
    }
}