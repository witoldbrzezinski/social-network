package pl.witoldbrzezinski.SocialNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.witoldbrzezinski.SocialNetwork.entity.Role;
import pl.witoldbrzezinski.SocialNetwork.entity.RoleEnum;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByRole(RoleEnum roleEnum);

}