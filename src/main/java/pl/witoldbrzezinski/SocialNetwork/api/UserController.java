package pl.witoldbrzezinski.SocialNetwork.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.witoldbrzezinski.SocialNetwork.entity.AppUser;
import pl.witoldbrzezinski.SocialNetwork.entity.Role;
import pl.witoldbrzezinski.SocialNetwork.entity.RoleEnum;
import pl.witoldbrzezinski.SocialNetwork.service.AppUserService;
import pl.witoldbrzezinski.SocialNetwork.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final AppUserService userService;

    Utils utils = new Utils();

    @GetMapping("/users")
    public ResponseEntity<List<AppUser>>getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<AppUser>saveUser(@RequestBody AppUser user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/user/save").toUriString());
        ResponseEntity<AppUser> userResponseEntity = ResponseEntity.created(uri).body(userService.saveUser(user));
        userService.addRoleToUser(user.getUsername(),RoleEnum.ROLE_USER);
        return userResponseEntity;
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role>saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addToUser")
    public ResponseEntity<?>addRoleToUser(@RequestBody RoleToUserForm form){
        userService.addRoleToUser(form.getUsername(),form.getRoleEnum());
        return ResponseEntity.ok().build();
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                JWTVerifier verifier = JWT.require(utils.getDefaultJWTAlgorithm()).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                AppUser user = userService.getUser(username);
                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(role -> role.getRole().toString()).collect(Collectors.toList()))
                        .sign(utils.getDefaultJWTAlgorithm());
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken",accessToken);
                tokens.put("refreshToken",refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            } catch (Exception exception) {
                exception.printStackTrace();
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("errorMessage",exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }
        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}


@Data
class RoleToUserForm {
    private String username;
    private RoleEnum roleEnum;
}
