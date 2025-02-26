package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.exception.AuthorNotFoundException;
import org.joyapi.model.Author;
import org.joyapi.model.Post;
import org.joyapi.repos.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class AuthorService {
    private AuthorRepository authorRepository;

    public Author newAuthor(String authorName){
        Author author = new Author();
        author.setName(authorName);
        author.setPostAmount(0l);

        return authorRepository.save(author);
    }

    public void oneMorePost(Post post){
        List<String> authorNames = recognizeAuthors(post.getAuthors());

        for(String authorName : authorNames) {
            Author author = authorRepository.findByName(authorName)
                    .orElseThrow(() -> new AuthorNotFoundException("Cannot find author " + authorName));
            author.oneMorePost();
            authorRepository.save(author);
        }
    }

    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    public List<String> recognizeAuthors(String inputAuthors){
        return Stream.of(inputAuthors.split(" "))
                    .filter(authorRepository::existsByName)
                    .toList();
    }
}
