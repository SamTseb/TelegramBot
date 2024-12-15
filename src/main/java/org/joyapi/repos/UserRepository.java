package org.joyapi.repos;

import org.joyapi.model.Author;
import org.joyapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u.favoriteAuthors FROM User u WHERE u.id = :userId")
    Set<Author> findFavoriteAuthorsByUserId(@Param("userId") Long userId);
}
