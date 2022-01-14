package ga.rugal.pt.springmvc.graphql;

import ga.rugal.glavenus.graphql.PostDto;
import ga.rugal.glavenus.graphql.PostTagDto;
import ga.rugal.glavenus.graphql.PostTagResolver;
import ga.rugal.glavenus.graphql.TagDto;
import ga.rugal.pt.core.service.PostTagService;
import ga.rugal.pt.springmvc.mapper.PostMapper;
import ga.rugal.pt.springmvc.mapper.TagMapper;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Field resolver for PostTag.
 *
 * @author Rugal Bernstein
 */
@Component
public class PostTagFieldResolver implements GraphQLResolver<PostTagDto>, PostTagResolver {

  @Autowired
  private PostTagService postTagService;

  @Override
  public PostDto post(final PostTagDto postTagDto) throws Exception {
    return PostMapper.I.from(this.postTagService.getDao()
      .findById(postTagDto.getPtid()).get().getPost());
  }

  @Override
  public TagDto tag(final PostTagDto postTagDto) throws Exception {
    return TagMapper.I.from(this.postTagService.getDao()
      .findById(postTagDto.getPtid()).get().getTag());
  }
}
