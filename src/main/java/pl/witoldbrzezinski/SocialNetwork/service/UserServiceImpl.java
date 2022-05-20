package pl.witoldbrzezinski.SocialNetwork.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.witoldbrzezinski.SocialNetwork.entity.AppUser;
import pl.witoldbrzezinski.SocialNetwork.entity.Role;
import pl.witoldbrzezinski.SocialNetwork.entity.RoleEnum;
import pl.witoldbrzezinski.SocialNetwork.repository.AppUserRepository;
import pl.witoldbrzezinski.SocialNetwork.repository.RoleRepository;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements AppUserService{

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AppUser saveUser(AppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, RoleEnum roleEnum) {
        AppUser user = userRepository.findByUsername(username);
        Role role = roleRepository.findByRole(roleEnum);
        user.getRoles().add(role);
    }

    @Override
    public AppUser getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<AppUser> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<AppUser> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }


}
