package org.joyapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "guid", nullable = false)
    private UUID guid;

    @Column(name = "fileUrl", nullable = false)
    private String fileUrl;

    @Column(nullable = false, unique = true)
    private int postId;

    @Column
    private int score;

    @Column(nullable = false)
    private String tags;
}
