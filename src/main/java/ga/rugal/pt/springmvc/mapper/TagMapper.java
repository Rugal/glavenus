package ga.rugal.pt.springmvc.mapper;

import ga.rugal.glavenus.graphql.NewTagDto;
import ga.rugal.glavenus.graphql.TagDto;
import ga.rugal.pt.core.entity.Tag;

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
public interface TagMapper {

  TagMapper I = Mappers.getMapper(TagMapper.class);

  Tag to(NewTagDto newTagDto);

  Tag to(TagDto tagDto);

  TagDto from(Tag tag);
}
