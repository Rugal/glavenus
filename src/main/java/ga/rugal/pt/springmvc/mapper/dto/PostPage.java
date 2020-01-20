package ga.rugal.pt.springmvc.mapper.dto;

import java.util.List;

import ga.rugal.pt.core.entity.Post;

import lombok.Value;

/**
 * DTO for post pagination.
 *
 * @author Rugal Bernstein
 */
@Value
public class PostPage {

  private List<Post> items;

  /**
   * size of each page.
   */
  private int size;

  /**
   * 0 based index of page. minimum: 0.
   */
  private int index;

  /**
   * number of total page. minimum: 1.
   */
  private int total;
}
