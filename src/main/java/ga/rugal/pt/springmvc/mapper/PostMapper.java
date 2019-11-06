package ga.rugal.pt.springmvc.mapper;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.openapi.model.NewPostDto;
import ga.rugal.pt.openapi.model.PostDto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For Course.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class)
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface PostMapper {

  PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

  Post to(NewPostDto newPostDto);

  Post to(PostDto postDto);

  PostDto from(Post post);
}
