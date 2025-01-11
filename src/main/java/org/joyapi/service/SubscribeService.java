package org.joyapi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Service class for updating post to users.
 */
@Slf4j
@AllArgsConstructor
@Service
public class SubscribeService {
    private final TelegramBot telegramBot;
    private final ImageDownloadService imageDownloadService;
    private final PostService postService;
    private final UserService userService;

    /**
     * Subscribes users to new posts from their favorite authors.
     * It is scheduled to run every hour.
     */
    @Transactional
//    @Scheduled(cron = "0 0 * * * *")
    @Scheduled(cron = "0 */5 * * * *")
    public void subscribe() {
        List<User> users = userService.getUsers();
        for (User user : users) {
            log.info("New post for a user with ID {}", user.getId());
            Set<Author> allAuthors = user.getFavoriteAuthors();
            for (Author author : allAuthors) {
                List<Post> posts = postService.getAndSavePosts(100, 0, author.getName());

                if(posts.isEmpty()) {
                    continue;
                }

                log.info("New post of {}", author.getName());
                telegramBot.sendTextMessageByUserId("New post of " + author.getName(), user.getId());
                for (Post post : posts) {
                    String imageUrl = post.getFileUrl();
                    File image = imageDownloadService.downloadImage(imageUrl);
                    telegramBot.sendImage(image, user.getChatID());
                    telegramBot.sendReactionMessage(post.getPostId(), user.getChatID());
                }
                log.info("End of new post of {}", author.getName());
            }
        }
    }
}