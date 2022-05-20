package pl.witoldbrzezinski.SocialNetwork.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import pl.witoldbrzezinski.SocialNetwork.entity.AppUser;
import pl.witoldbrzezinski.SocialNetwork.repository.AppUserRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserServiceImplTest {

    @Autowired
    private AppUserRepository userRepository;

    private AppUser user;


    @BeforeEach
    public void init() {
        user = new AppUser("Test","1234",null,1,"test@test.com","Test","Test", Collections.emptyList());
    }

    @Test
    public void savingUser(){
        AppUser savedUser = userRepository.save(user);
        assertThat(savedUser).isNotNull();

    }

}
