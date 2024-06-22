package com.sparta.javajyojo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "password_historys")
public class PasswordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long pwHistoryId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime changeDate;

    public PasswordHistory(User user, String password) {
        this.user = user;
        this.password = password;
        this.changeDate = LocalDateTime.now();
    }
}
