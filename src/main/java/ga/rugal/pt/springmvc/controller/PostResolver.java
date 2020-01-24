package ga.rugal.pt.springmvc.controller;

import java.util.List;
import java.util.stream.Collectors;

import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.Tag;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.PostTagService;
import ga.rugal.pt.core.service.ReviewService;

import com.coxautodev.graphql.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Post query resolver.
 *
 * @author Rugal Bernstein
 */
@Component
public class PostResolver implements GraphQLResolver<Post> {

  @Autowired
  private ReviewService reviewService;

  @Autowired
  private PostTagService postTagService;

  public User author(final Post p) {
    return p.getAuthor();
  }

  public Double rate(final Post p) {
    return this.reviewService.getDao().getRateByPost(p);
  }

  public List<Tag> tags(final Post p) {
    return this.postTagService.getDao().findByPost(p).stream()
            .map(pt -> pt.getTag())
            .collect(Collectors.toList());
  }
}
