package ga.rugal.pt.springmvc.mapper;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.openapi.model.PostPageDto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

/**
 * The Data Mapper For Course.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class)
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface PostPageMapper {

  PostPageMapper INSTANCE = Mappers.getMapper(PostPageMapper.class);

  default PostPageDto from(final Page<Post> page) {
    final PostPageDto dto = new PostPageDto();
    dto.setIndex(page.getNumber());
    dto.setSize(page.getSize());
    dto.setTotal(page.getTotalPages());
    page.forEach(p -> dto.addItemsItem(PostMapper.INSTANCE.from(p)));
    return dto;
  }
}
