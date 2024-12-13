package org.joyapi.service.eternal;

import com.source.client.api.DefaultApi;
import com.source.client.model.PostDTO;
import lombok.AllArgsConstructor;
import org.joyapi.mapper.PostMapper;
import org.joyapi.model.Post;
import org.jvnet.hk2.annotations.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RuleExternalService {
    private final DefaultApi defaultApi;
    private final PostMapper postMapper;

    public Post getPost(Integer postId) {
        PostDTO postDTO = defaultApi.indexPhpGet("dapi", "post", "index",
                                                null, null, null, null, null,
                                                        postId, 1, null)
                                    .stream()
                                    .findFirst()
                                    .orElse(null);

        return postMapper.toPost(postDTO);
    }

    public List<Post> getPostList(Integer limit, Integer pageNumber, String tags) {
        List<PostDTO> postDTOs = defaultApi.indexPhpGet("dapi", "post", "index", null, limit, pageNumber, tags,
                                null, null, 1, null);

        return postMapper.toPostList(postDTOs);
    }
}
