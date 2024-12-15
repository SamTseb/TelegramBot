package org.joyapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

    public void oneMorePost(){
        postAmount++;
    }
}
