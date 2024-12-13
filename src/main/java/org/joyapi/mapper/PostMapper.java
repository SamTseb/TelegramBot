package org.joyapi.mapper;

import com.source.client.model.PostDTO;
import org.joyapi.model.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {

    Post toPost(PostDTO postDTO);
    List<Post> toPostList(List<PostDTO> postDTOs);
}
