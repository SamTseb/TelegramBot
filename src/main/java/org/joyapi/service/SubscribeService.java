package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.bot.TelegramBot;
import org.joyapi.model.Author;
import org.joyapi.model.Post;
import org.joyapi.service.eternal.PostExternalService;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.List;

@AllArgsConstructor
@Service
public class SubscribeService {
    private final TelegramBot telegramBot;
    private final ImageDownloadService imageDownloadService;
    private final PostService postService;
    private final AuthorService authorService;

    @Scheduled(fixedDelay = 5000)
    public void subscribe(){
        List<Author> allAuthors = authorService.getAllAuthors();
        for(Author author : allAuthors) {
            List<Post> posts = postService.getAndSavePosts(100, 0, author.getName());
            for (Post post : posts) {
                String imageUrl = post.getFileUrl();
                File image = imageDownloadService.downloadImage(imageUrl);
                telegramBot.sendImage(image);
                telegramBot.sendReactionMessage(post.getPostId());
            }
        }
    }
}
