package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.mapper.UserMapper;
import org.joyapi.model.User;
import org.joyapi.repos.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
//    private final PostService postService;

    public User addNewUser(Message message) {
        User user = userMapper.extractUser(message);
        return userRepository.save(user);
    }

    private void syncPost(User user){
//        postService.getAndSavePosts(100, 0, author.getName());
    }
}
