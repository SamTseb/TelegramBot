package org.joyapi.service.eternal;

import com.source.client.api.DefaultApi;
import com.source.client.model.PostDTO;
import lombok.AllArgsConstructor;
import org.joyapi.mapper.PostMapper;
import org.joyapi.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class PostExternalService {
    private final DefaultApi defaultApi;
    private final PostMapper postMapper;

    public Post getPost(String postId) {
        PostDTO postDTO = defaultApi.getPosts("dapi", "post", "index",
                                                null, null, null, null,
                                                        postId, "1", null, null)
                                    .stream()
                                    .findFirst()
                                    .orElse(null);

        return postMapper.toPost(postDTO);
    }

    public List<Post> getPostList(Integer limit, Integer pageNumber, String tags) {
        List<PostDTO> postDTOs = defaultApi.getPosts("dapi", "post", "index", limit, pageNumber, tags,
                                null, null, "1", null, null);

        return postMapper.toPostList(postDTOs);
    }

    public List<Post> getPostList(String tags){
        return getPostList(100, 0, tags);
    }

    public void addPostToFavorites(String postId) {
        defaultApi.addPostToFavorites(postId);
    }
}
