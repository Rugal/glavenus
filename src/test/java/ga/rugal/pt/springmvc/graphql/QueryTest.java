package ga.rugal.pt.springmvc.graphql;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import ga.rugal.UnitTestBase;
import ga.rugal.pt.core.dao.PostDao;
import ga.rugal.pt.core.dao.TagDao;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.Tag;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.TagService;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTest;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@GraphQLTest
public class QueryTest extends UnitTestBase {

  @Autowired
  private GraphQLTestTemplate template;

  @Autowired
  private Post post;

  @Autowired
  private Tag tag;

  @MockBean
  private TagDao tagDao;

  @MockBean
  private TagService tagService;

  @MockBean
  private PostDao postDao;

  @MockBean
  private PostService postService;

  @BeforeEach
  public void setUp() {
    when(this.postService.getDao()).thenReturn(this.postDao);

    when(this.tagService.getDao()).thenReturn(this.tagDao);

    when(this.postDao.findById(any())).thenReturn(Optional.of(this.post));

    when(this.tagDao.findById(any())).thenReturn(Optional.of(this.tag));
  }

  @Test
  public void getPost_null() throws Exception {
    when(this.postDao.findById(any())).thenReturn(Optional.empty());

    final GraphQLResponse r = this.template.postForResource("graphql/getPost.graphql");

    Assertions.assertNotNull(r);
    Assertions.assertTrue(r.isOk());
    Assertions.assertNull(r.get("$.data.getPost"));
    verify(this.postDao).findById(any());
  }

  @Test
  public void getPost() throws Exception {
    final GraphQLResponse r = this.template.postForResource("graphql/getPost.graphql");

    Assertions.assertNotNull(r);
    Assertions.assertTrue(r.isOk());
    Assertions.assertEquals(this.post.getTitle(), r.get("$.data.getPost.title"));
    verify(this.postDao).findById(any());
  }

  @Test
  public void getTag_null() throws Exception {
    when(this.tagDao.findById(any())).thenReturn(Optional.empty());

    final GraphQLResponse r = this.template.postForResource("graphql/getTag.graphql");

    Assertions.assertNotNull(r);
    Assertions.assertTrue(r.isOk());
    Assertions.assertNull(r.get("$.data.getTag"));
    verify(this.tagDao).findById(any());
  }

  @Test
  public void getTag() throws Exception {
    final GraphQLResponse r = this.template.postForResource("graphql/getTag.graphql");

    Assertions.assertNotNull(r);
    Assertions.assertTrue(r.isOk());
    Assertions.assertEquals(this.tag.getName(), r.get("$.data.getTag.name"));
    verify(this.tagDao).findById(any());
  }
}
