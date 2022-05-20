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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
        mockMvc.perform(post("/api/user/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated());
    }

    @Test
    public void postingNewRoleShouldCreateRole() throws Exception {
        Role role = new Role(3L, RoleEnum.ROLE_TEST);
        when(userService.saveRole(any(Role.class))).thenReturn(role);
        mockMvc.perform(post("/api/role/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated());
    }


}
