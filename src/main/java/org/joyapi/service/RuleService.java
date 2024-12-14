package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.bot.TelegramBot;
import org.joyapi.model.Post;
import org.joyapi.model.enums.AuthorTag;
import org.joyapi.service.eternal.PostExternalService;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.util.List;

@AllArgsConstructor
@Service
public class RuleService {
    private final PostExternalService postExternalService;
    private final TelegramBot telegramBot;
    private final ImageDownloadService imageDownloadService;
    private final PostService postService;

    @Scheduled(fixedDelay = 10000)
    public void getPosts(){
        List<Post> posts = getAndSavePosts(5,0, AuthorTag.kawa.getValue());
        List<String> imageUrls = posts.stream().map(Post::getFileUrl).toList();
        List<File> images = imageDownloadService.downloadImageList(imageUrls);
        telegramBot.sendImageList(images);
    }

    private List<Post> getAndSavePosts(Integer limit, Integer pageNumber, String tags){
        List<Post> posts = postExternalService.getPostList(limit, pageNumber, tags);
        List<Post> savedPosts = savePosts(posts);

        return savedPosts;
    }

    private List<Post> savePosts(List<Post> posts){
        List<Post> savedPosts = posts.stream()
                .filter(post -> !postService.doesPostExist(post.getId()))
                .toList();
        savedPosts.forEach(postService::savePost);

        return savedPosts;
    }
}
