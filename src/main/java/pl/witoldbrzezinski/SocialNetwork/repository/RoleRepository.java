package pl.witoldbrzezinski.SocialNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.witoldbrzezinski.SocialNetwork.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(String role);

}