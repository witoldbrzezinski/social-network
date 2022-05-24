package pl.witoldbrzezinski.SocialNetwork.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import pl.witoldbrzezinski.SocialNetwork.entity.AppUser;
import pl.witoldbrzezinski.SocialNetwork.entity.Role;
import pl.witoldbrzezinski.SocialNetwork.entity.RoleEnum;
import pl.witoldbrzezinski.SocialNetwork.service.UserServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserControllerTest.class)
@AutoConfigureTestDatabase
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserServiceImpl userService;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    AppUser user;

    @BeforeEach
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        user = new AppUser("Test","1234",null,1,"test@test.com","Test","Test", Collections.emptyList());
        user.setId(1L);
    }

    @Test
    public void gettingAllUsersShouldReturnUsersJson() throws Exception {
        List<AppUser> users  = Arrays.asList(user);
        given(userService.getUsers()).willReturn(users);
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username",is(user.getUsername())));
    }

    @Test
    public void postingNewUserShouldCreateUser() throws Exception {
        when(userService.saveUser(any(AppUser.class))).thenReturn(user);
        mockMvc.perform(post("/api/users/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void postingNewRoleShouldCreateRole() throws Exception {
        Role role = new Role(3L, RoleEnum.ROLE_TEST);
        when(userService.saveRole(any(Role.class))).thenReturn(role);
        mockMvc.perform(post("/api/roles/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated());
    }

    @Test
    public void gettingUserByIdShouldReturnUser() throws Exception {
        long id = user.getId();
        when(userService.getUserById(id)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/users/{id}",id)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username",is(user.getUsername())));
    }

    @Test
    public void updatingUserShouldUpdateUser() throws Exception {
        long id = user.getId();
        AppUser updatedUser = new AppUser("Update","1234",null,1,"test@test.com","Update","Update", Collections.emptyList());
        when(userService.getUserById(id)).thenReturn(Optional.of(user));
        when(userService.saveUser(any(AppUser.class))).thenReturn(updatedUser);
        mockMvc.perform(put("/api/users/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username",is(updatedUser.getUsername())));

    }

    @Test
    public void deletingUserByIdShouldDeleteUserAndReturn204Response() throws Exception {
        long id = user.getId();
        doNothing().when(userService).deleteUserById(id);
        mockMvc.perform(delete("/api/users/{id}",id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }



}
