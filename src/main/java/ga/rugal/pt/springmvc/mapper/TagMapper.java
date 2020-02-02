package ga.rugal.pt.springmvc.mapper;

import ga.rugal.pt.core.entity.Tag;
import ga.rugal.pt.openapi.model.NewTagDto;
import ga.rugal.pt.openapi.model.TagDto;

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

  TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

  Tag to(NewTagDto newTagDto);

  Tag to(TagDto tagDto);

  TagDto from(Tag tag);
}
