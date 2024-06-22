package com.sparta.javajyojo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @Length(min = 4, max = 10)
    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String name;

    private String intro;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;

    private String refreshToken;

    public User(String username, String password, String name, String intro, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.intro = intro;
        this.role = role;
    }

    public void signOut() {
        this.role = UserRoleEnum.WITHOUT;
    }

    public void logOut() {
        refreshToken = null;
    }

    public void update(Optional<String> newPassword, Optional<String> name, Optional<String> intro) {
        this.password = newPassword.orElse(this.password);
        this.name = name.orElse(this.name);
        this.intro = intro.orElse(this.intro);
    }

    public void updateToken(String refreshToken){ this.refreshToken = refreshToken; }

}