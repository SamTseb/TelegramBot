package org.joyapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column
    private Long id;

    @Column(name = "is_bot", nullable = false)
    private boolean isBot;

    @Column(name = "join_at", nullable = false)
    private LocalDateTime joinAt;

    @Column(name = "chat_id", nullable = false)
    private Long chatID;

    @Column(nullable = false)
    private String cookies;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_author_relationship",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "author_name")
    )
    private Set<Author> favoriteAuthors = new HashSet<>();

    @Column(name = "prohibited_tags")
    private String prohibitedTags;
}
