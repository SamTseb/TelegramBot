package org.joyapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;

    @Column(name = "is_bot", nullable = false)
    private boolean isBot;

    @Column(name = "join_at", nullable = false)
    private LocalDateTime joinAt;
}
