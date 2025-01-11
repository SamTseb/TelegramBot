package org.joyapi.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joyapi.model.Post;
import org.joyapi.repos.PostRepository;
import org.joyapi.service.eternal.PostExternalService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service class for managing posts.
 */
@Slf4j
@AllArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final AuthorService authorService;
    private final PostExternalService postExternalService;

    /**
     * Saves a post.
     *
     * @param post the post to save
     * @return the saved post
     */
    public Post savePost(Post post) {
        authorService.oneMorePost(post);
        return postRepository.save(post);
    }

    /**
     * Retrieves all posts.
     *
     * @return a list of all posts
     */
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param guid the ID of the post
     * @return the post with the given ID, or null if not found
     */
    public Post getPostById(UUID guid) {
        return postRepository.findById(guid).orElse(null);
    }

    /**
     * Deletes a post by its ID.
     *
     * @param guid the ID of the post to delete
     */
    public void deletePost(UUID guid) {
        postRepository.deleteById(guid);
    }

    /**
     * Checks if a post exists by its post ID.
     *
     * @param id the post ID to check
     * @return true if the post exists, false otherwise
     */
    public boolean doesPostExist(String id) {
        return postRepository.existsByPostId(id);
    }

    /**
     * Adds a post to the user's favorites.
     *
     * @param postId the ID of the post to add to favorites
     * @param userCookies the user's cookies
     */
    public void addPostToFavorites(String postId, String userCookies){
        postExternalService.addPostToFavorites(postId, userCookies);
    }

    /**
     * Retrieves and saves posts based on the given parameters.
     *
     * @param limit the maximum number of posts to retrieve
     * @param pageNumber the page number to retrieve
     * @param tags the tags to filter posts by
     * @return a list of saved posts
     */
    public List<Post> getAndSavePosts(Integer limit, Integer pageNumber, String tags){
        List<Post> posts = postExternalService.getPostList(limit, pageNumber, tags);
        return savePosts(posts);
    }

    /**
     * Saves a list of posts.
     *
     * @param posts the list of posts to save
     * @return a list of saved posts
     */
    private List<Post> savePosts(List<Post> posts){
        List<Post> savedPosts = posts.stream()
                .filter(post -> !doesPostExist(post.getPostId()))
                .toList();
        logNewPosts(savedPosts);
        savedPosts.forEach(this::savePost);

        return savedPosts;
    }

    /**
     * Logs the IDs of new posts if the list of posts is not empty.
     *
     * @param posts the list of posts to be logged
     */
    private void logNewPosts(List<Post> posts) {
        if (!posts.isEmpty()){
            List<String> newPostIds = posts.stream()
                    .map(Post::getPostId)
                    .toList();
            String newPostMessage = "New posts:\n" + String.join("\n", newPostIds);
            log.info(newPostMessage);
        }
    }
}