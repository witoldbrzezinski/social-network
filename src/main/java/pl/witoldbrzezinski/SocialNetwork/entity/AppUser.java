package pl.witoldbrzezinski.SocialNetwork.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name="users")
public class AppUser {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="user_id")
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
    private String lastName;

    @ManyToMany(fetch = EAGER, cascade={CascadeType.DETACH,CascadeType.MERGE,CascadeType.REFRESH,CascadeType.PERSIST})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.ALL,mappedBy = "friendsOf")
    @JsonIgnore
    private Set<AppUser> friends = new HashSet<>();


    @JoinTable(name = "friends",
    joinColumns = @JoinColumn(name= "user_id"),
    inverseJoinColumns = @JoinColumn(name = "friend_id"))
    @ManyToMany(cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<AppUser> friendsOf = new HashSet<>();

    public AppUser(String username, String password, String matchingPassword, int enabled,
                   String email, String firstName, String lastName, Collection<Role> roles) {
        this.username = username;
        this.password = password;
        this.matchingPassword = matchingPassword;
        this.enabled = enabled;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
    }

    public void addFriend(AppUser friendToAdd){
        friendsOf.add(friendToAdd);
        friendToAdd.getFriends().add(this);
    }

    public void removeFriend(AppUser friendToRemove){
        friendsOf.remove(friendToRemove);
        friendToRemove.getFriends().remove(this);
    }
}
