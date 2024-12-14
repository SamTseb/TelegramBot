package org.joyapi.service;

import lombok.AllArgsConstructor;
import org.joyapi.exception.AuthorNotFoundException;
import org.joyapi.model.Author;
import org.joyapi.model.Post;
import org.joyapi.repos.AuthorRepository;
import org.joyapi.repos.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AuthorService authorService;

    public Post savePost(Post post) {
        authorService.oneMorePost(post);
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post getPostById(UUID guid) {
        return postRepository.findById(guid).orElse(null);
    }

    public void deletePost(UUID guid) {
        postRepository.deleteById(guid);
    }

    public boolean doesPostExist(String id) {
        return postRepository.existsByPostId(id);
    }
}
