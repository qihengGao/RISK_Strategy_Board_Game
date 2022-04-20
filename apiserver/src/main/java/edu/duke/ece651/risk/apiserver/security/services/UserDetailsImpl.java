package edu.duke.ece651.risk.apiserver.security.services;

import edu.duke.ece651.risk.apiserver.models.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String username;
    private String email;
    @JsonIgnore
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    private Long elo;

    public Long getElo() {
        return elo;
    }

    public void setElo(Long elo) {
        this.elo = elo;
    }

    /**
     * default constructor
     * @param id
     * @param username
     * @param email
     * @param password
     * @param authorities
     */
    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities,Long elo) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.elo = elo;
    }

    /**
     * buidl a UserDetailsImpl from a user
     * @param user
     * @return constructed UserDetailsImpl
     */
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.getElo());
    }

    /**
     * get granted authority
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * get id
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * get email
     * @return email
     */
    public String getEmail() {
        return email;
    }

    /**
     * get password
     * @return password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * get username
     * @return username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * check if account expires
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * check if account is not locked
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * check if credentials not expired
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * check if enabled
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * equal operator
     * @param o
     * @return true if equals, otherwise false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
