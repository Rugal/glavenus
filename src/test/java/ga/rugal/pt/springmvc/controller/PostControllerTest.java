package ga.rugal.pt.springmvc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Optional;

import config.Constant;

import ga.rugal.glavenus.graphql.NewPostDto;
import ga.rugal.pt.core.dao.PostDao;
import ga.rugal.pt.core.dao.UserDao;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.core.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author rugal
 */
@Disabled
@WebMvcTest(PostController.class)
public class PostControllerTest extends ControllerUnitTestBase {

  @Mock
  private MockMultipartFile mock;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  public MockMultipartFile mmf;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Post post;

  @Autowired
  private User user;

  @Autowired
  private NewPostDto newPostDto;

  @Autowired
  private PostService postService;

  @Autowired
  private PostDao postDao;

  @Autowired
  private UserService userService;

  @Autowired
  private UserDao userDao;

  @Autowired
  private PostController controller;

  @BeforeEach
  @SneakyThrows
  public void setUp() {
    this.controller.setPostService(this.postService);
    this.controller.setUserService(this.userService);
    //
    given(this.postService.getDao()).willReturn(this.postDao);
    //
    given(this.postDao.findById(any())).willReturn(Optional.of(this.post));
    given(this.postDao.save(any())).willReturn(this.post);
    //
    given(this.userService.getDao()).willReturn(this.userDao);
    //
    given(this.userDao.findById(any())).willReturn(Optional.of(this.user));
    //
    given(this.mock.getName()).willReturn("file");
    given(this.mock.getBytes()).willThrow(IOException.class);
  }

  @SneakyThrows
  @Test
  public void upload_201() {
    this.mockMvc.perform(multipart("/post/1/torrent")
      .file(this.mmf)
      .header(Constant.UID, "1")
      .header(Constant.PASSWORD, "1"))
      .andExpect(status().isCreated());
    verify(this.postDao, times(1)).findById(any());
    verify(this.postDao, times(1)).save(any());
  }

  @SneakyThrows
  @Test
  public void upload_404() {
    given(this.postDao.findById(any())).willReturn(Optional.empty());

    this.mockMvc.perform(multipart("/post/1/torrent")
      .file(this.mmf)
      .header(Constant.UID, "1")
      .header(Constant.PASSWORD, "1"))
      .andExpect(status().isNotFound());
    verify(this.postDao, times(1)).findById(any());
    verify(this.postDao, never()).save(any());
  }

  @SneakyThrows
  @Test
  public void upload_422() {
    this.mockMvc.perform(multipart("/post/1/torrent")
      .file(this.mock)
      .header(Constant.UID, "1")
      .header(Constant.PASSWORD, "1"))
      .andExpect(status().isUnprocessableEntity());
    verify(this.postDao, times(1)).findById(any());
    verify(this.postDao, never()).save(any());
  }
}
