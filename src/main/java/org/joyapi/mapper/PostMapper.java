package org.joyapi.mapper;

import com.source.client.model.PostDTO;
import org.joyapi.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface PostMapper {

    @Mapping(target = "guid", ignore = true)
    @Mapping(target = "postId", source = "id")
    Post toPost(PostDTO postDTO);

    List<Post> toPostList(List<PostDTO> postDTOs);
}
