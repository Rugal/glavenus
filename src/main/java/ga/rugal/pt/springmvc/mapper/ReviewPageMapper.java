package ga.rugal.pt.springmvc.mapper;

import ga.rugal.glavenus.graphql.ReviewPageDto;
import ga.rugal.pt.springmvc.mapper.dto.ReviewPage;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For ReviewPage.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class, uses = {ReviewMapper.class})
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface ReviewPageMapper {

  ReviewPageMapper I = Mappers.getMapper(ReviewPageMapper.class);

  ReviewPageDto from(ReviewPage p);
}
