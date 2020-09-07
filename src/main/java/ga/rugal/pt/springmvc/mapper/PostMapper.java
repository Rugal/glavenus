package ga.rugal.pt.springmvc.mapper;

import ga.rugal.glavenus.graphql.NewPostDto;
import ga.rugal.glavenus.graphql.PostDto;
import ga.rugal.pt.core.entity.Post;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For Post.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class, uses = {UserMapper.class, PostTagMapper.class})
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface PostMapper {

  PostMapper I = Mappers.getMapper(PostMapper.class);

  Post to(NewPostDto newPostDto);

  Post to(PostDto postDto);

  PostDto from(Post post);
}
