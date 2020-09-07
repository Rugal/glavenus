package ga.rugal.pt.springmvc.mapper;

import ga.rugal.glavenus.graphql.PostTagDto;
import ga.rugal.pt.core.entity.PostTag;

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
public interface PostTagMapper {

  PostTagMapper I = Mappers.getMapper(PostTagMapper.class);

  PostTagDto from(PostTag postTag);
}
