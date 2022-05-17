package pl.witoldbrzezinski.SocialNetwork.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity
@Getter @Setter @NoArgsConstructor
public class AppUser {

    @Id
    private Long id;
    @Column(name = "username", unique=true)
    private String username;
    @Column(name = "password")
    private String password;
    @Transient
    private String matchingPassword;
    @Column(name = "enabled")
    private int enabled;
    @Column(name = "email", unique=true)
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lasName;


}
