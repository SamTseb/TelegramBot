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

@Slf4j
@AllArgsConstructor
@Service
public class SubscribeService {
    private final TelegramBot telegramBot;
    private final ImageDownloadService imageDownloadService;
    private final PostService postService;
    private final UserService userService;

    // TODO It's execited in other thread than bot processes. It intersects with them!
    @Transactional
    @Scheduled(cron = "0 0 * * * *")
    public void subscribe(){
        List<User> users = userService.getUsers();
        for(User user : users) {
            log.info("New post for a user with ID {}", user.getId());
            Set<Author> allAuthors = user.getFavoriteAuthors();
            for (Author author : allAuthors) {
                log.info("New post of {}", author.getName());
                telegramBot.sendTextMessageByUserId("New post of " + author.getName(), user.getId());
                List<Post> posts = postService.getAndSavePosts(100, 0, author.getName());
                for (Post post : posts) {
                    String imageUrl = post.getFileUrl();
                    File image = imageDownloadService.downloadImage(imageUrl);
                    telegramBot.sendImage(image, user.getChatID());
                    telegramBot.sendReactionMessage(post.getPostId(), user.getChatID());
                }
                log.info("End  of new post of {}", author.getName());
            }
        }
    }
}
