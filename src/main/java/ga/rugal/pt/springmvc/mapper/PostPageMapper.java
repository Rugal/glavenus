package ga.rugal.pt.springmvc.mapper;

import ga.rugal.glavenus.graphql.PostPageDto;
import ga.rugal.pt.springmvc.mapper.dto.PostPage;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For PostPage.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class, uses = {PostMapper.class})
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface PostPageMapper {

  PostPageMapper I = Mappers.getMapper(PostPageMapper.class);

  PostPageDto from(PostPage p);
}
