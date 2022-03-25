package edu.duke.ece651.risk.apiserver.repository;


import edu.duke.ece651.risk.apiserver.models.ERole;
import edu.duke.ece651.risk.apiserver.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}