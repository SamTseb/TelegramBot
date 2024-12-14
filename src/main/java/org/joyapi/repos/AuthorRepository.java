package org.joyapi.repos;

import org.joyapi.model.Author;
import org.joyapi.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorRepository extends JpaRepository<Author, UUID> {

    Optional<Author> findByName(String name);
    boolean existsByName(String name);
}
