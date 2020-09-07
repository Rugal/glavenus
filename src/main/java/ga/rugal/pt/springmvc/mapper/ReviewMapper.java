package ga.rugal.pt.springmvc.mapper;

import ga.rugal.glavenus.graphql.ReviewDto;
import ga.rugal.pt.core.entity.Review;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For Post.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class, uses = {UserMapper.class, PostMapper.class})
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface ReviewMapper {

  ReviewMapper I = Mappers.getMapper(ReviewMapper.class);

  ReviewDto from(Review b);
}
