package org.joyapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "guid")
    private UUID guid;

    @Column(name = "file_url", nullable = false)
    private String fileUrl;

    @Column(name = "post_id", nullable = false, unique = true)
    private Long postId;

    @Column
    private int score;

    @Column(nullable = false)
    private String authors;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}
