package pl.witoldbrzezinski.SocialNetwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.witoldbrzezinski.SocialNetwork.entity.AppUser;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
 //   Optional<AppUser> findById(Long id);

}