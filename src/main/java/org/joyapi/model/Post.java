package org.joyapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

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

    @JsonProperty("file_url")
    @Column(name = "fileUrl", nullable = false)
    private String fileUrl;

    @JsonProperty("id")
    @Column(nullable = false)
    private int id;

    @JsonProperty("score")
    @Column(nullable = true)
    private int score;

    @JsonProperty("tags")
    @Column(nullable = false)
    private String tags;
}
