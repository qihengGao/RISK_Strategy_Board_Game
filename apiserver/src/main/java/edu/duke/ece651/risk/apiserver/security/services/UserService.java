package edu.duke.ece651.risk.apiserver.security.services;

import edu.duke.ece651.risk.apiserver.models.User;
import edu.duke.ece651.risk.apiserver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;


    /**
     * get a UserDetails from a username
     */
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return user;
    }

    @Transactional
    public void setEloByUserID(Long ID, Long elo) throws UsernameNotFoundException {
        User user = userRepository.findByid(ID)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with ID: " + ID));
        user.setElo(elo);
    }



}
