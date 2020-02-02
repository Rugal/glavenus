package ga.rugal.pt.springmvc.mapper;

import ga.rugal.pt.core.entity.PostTag;
import ga.rugal.pt.openapi.model.PostTagDto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For Course.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class)
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface PostTagMapper {

  PostTagMapper INSTANCE = Mappers.getMapper(PostTagMapper.class);

  @Mapping(source = "postTag.post.pid", target = "pid")
  @Mapping(source = "postTag.tag.tid", target = "tid")
  PostTagDto from(PostTag postTag);
}
