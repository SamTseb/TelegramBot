package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.bot.TelegramBot;
import org.joyapi.model.Post;
import org.joyapi.model.enums.AuthorTag;
import org.joyapi.service.eternal.PostExternalService;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;

@AllArgsConstructor
@Service
public class RuleService {
    private final PostExternalService postExternalService;
    private final TelegramBot telegramBot;
    private final ImageDownloadService imageDownloadService;

    @Scheduled(fixedDelay = 2000)
    public void getPosts(){
        Post post = postExternalService.getPostList(1,0, AuthorTag.kawa.getValue()).get(0);
        File image = imageDownloadService.downloadImage(post.getFileUrl());
        telegramBot.sendImage(image);
    }
}
