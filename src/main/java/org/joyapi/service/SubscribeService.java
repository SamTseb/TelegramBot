package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.bot.TelegramBot;
import org.joyapi.model.Author;
import org.joyapi.model.Post;
import org.joyapi.model.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class SubscribeService {
    private final TelegramBot telegramBot;
    private final ImageDownloadService imageDownloadService;
    private final PostService postService;
    private final AuthorService authorService;
    private final UserService userService;

    // TODO It's execited in other thread than bot processes. It intersects with them!
    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void subscribe(){
        List<User> users = userService.getUsers();
        for(User user : users) {
            Set<Author> allAuthors = user.getFavoriteAuthors();
            for (Author author : allAuthors) {
                List<Post> posts = postService.getAndSavePosts(100, 0, author.getName());
                for (Post post : posts) {
                    String imageUrl = post.getFileUrl();
                    File image = imageDownloadService.downloadImage(imageUrl);
                    telegramBot.sendImage(image, user.getChatID());
                    telegramBot.sendReactionMessage(post.getPostId(), user.getChatID());
                }
            }
        }
    }
}
