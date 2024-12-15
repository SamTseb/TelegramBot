package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.exception.UserNotFoundException;
import org.joyapi.mapper.UserMapper;
import org.joyapi.model.Author;
import org.joyapi.model.User;
import org.joyapi.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PostService postService;
    private final AuthorService authorService;

    // TODO Check if already exists
    public void addNewUser(Message message, String cookies) {
        User user = userMapper.extractUser(message);
        user.setCookies(cookies);
        userRepository.save(user);
    }

    public void syncPost(Long userId){
        Set<Author> authors = getUsersAuthors(userId);
        for (Author author : authors) {
            postService.getAndSavePosts(100, 0, author.getName());
        }
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public User getUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }

    public void addPostToFavorites(String postId, Long userId){
        String userCookies = getUser(userId).getCookies();
        postService.addPostToFavorites(postId, userCookies);
    }

    public void newAuthor(String authorName, Long userId){
        User user = getUser(userId);
        Author author = authorService.newAuthor(authorName);
        Set<Author> authors = user.getFavoriteAuthors();
        authors.add(author);
        userRepository.save(user);
    }

    private Set<Author> getUsersAuthors(Long userId){
        return userRepository.findFavoriteAuthorsByUserId(userId);
    }
}
