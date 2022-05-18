package pl.witoldbrzezinski.SocialNetwork.service;

import pl.witoldbrzezinski.SocialNetwork.entity.AppUser;
import pl.witoldbrzezinski.SocialNetwork.entity.Role;
import pl.witoldbrzezinski.SocialNetwork.entity.RoleEnum;

import java.util.List;

public interface AppUserService {

    AppUser saveUser(AppUser user);
    Role saveRole(Role role);
    void addRoleToUser(String username, RoleEnum roleEnum);
    AppUser getUser(String username);
    List<AppUser> getUsers();
}

