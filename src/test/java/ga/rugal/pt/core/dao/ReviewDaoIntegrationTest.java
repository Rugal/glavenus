package ga.rugal.pt.core.dao;

import ga.rugal.IntegrationTestBase;
import ga.rugal.pt.core.entity.Post;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ReviewDaoIntegrationTest extends IntegrationTestBase {

  @Autowired
  private ReviewDao dao;

  private final Post post = new Post();

  @BeforeEach
  public void setUp() {
    this.post.setPid(1);
  }

  @Test
  public void testFindByPost() {
    Assertions.assertNotNull(this.dao.getRateByPost(post));
  }
}
