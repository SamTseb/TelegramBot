package org.joyapi.service;

import org.joyapi.exception.AuthorNotFoundException;
import org.joyapi.model.Author;
import org.joyapi.model.Post;
import org.joyapi.model.enums.AuthorTag;
import org.joyapi.repos.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class AuthorService {
    private AuthorRepository authorRepository;

    public void newAuthor(){}

    public void oneMorePost(Post post){
        List<String> authorNames = recognizeAuthors(post.getTags());

        for(String authorName : authorNames) {
            Author author = authorRepository.findByName(authorName)
                    .orElseThrow(() -> new AuthorNotFoundException("Cannot find author " + authorName));
            author.oneMorePost();
            authorRepository.save(author);
        }
    }

    private List<String> recognizeAuthors(String inputTags){
        return Stream.of(inputTags.split(" "))
                    .filter(AuthorTag::contains)
                    .toList();
    }
}
