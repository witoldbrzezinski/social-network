package pl.witoldbrzezinski.SocialNetwork.service;

import pl.witoldbrzezinski.SocialNetwork.entity.AppUser;
import pl.witoldbrzezinski.SocialNetwork.entity.Role;
import pl.witoldbrzezinski.SocialNetwork.entity.RoleEnum;

import java.util.List;
import java.util.Optional;

public interface AppUserService {

    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, RoleEnum roleEnum);
    AppUser getUserByUsername(String username);
    Optional<AppUser> getUserById(Long id);
    List<AppUser> getUsers();
    void deleteUserById(Long id);
    void addFriendToUser(String username, String friendName);
    void removeFriendFromUser(String username, String friendName);







}

