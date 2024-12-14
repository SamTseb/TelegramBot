package org.joyapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author {

    @Id
    @Column
    private String name;

    @Column(name = "post_amount")
    private Long postAmount;
}
