package ga.rugal.pt.springmvc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import config.SystemDefaultProperty;

import ga.rugal.pt.core.dao.PostDao;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.service.PostService;
import ga.rugal.pt.openapi.model.NewPostDto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

/**
 *
 * @author rugal
 */
@WebMvcTest(PostController.class)
public class PostControllerTest extends ControllerUnitTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  public File torrentFile;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private Post post;

  @Autowired
  private NewPostDto newPostDto;

  @Autowired
  private PostService postService;

  @Autowired
  private PostDao postDao;

  @Autowired
  private PostController controller;

  @BeforeEach
  public void setUp() {
    this.controller.setPostService(this.postService);

    given(this.postService.getDao()).willReturn(this.postDao);

    given(this.postDao.findById(any())).willReturn(Optional.of(this.post));
    given(this.postDao.save(any())).willReturn(this.post);
  }

  @SneakyThrows
  @Test
  public void createPost_201() {
    this.mockMvc.perform(post("/post")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .content(this.objectMapper.writeValueAsString(this.newPostDto))
            .accept(MediaType.APPLICATION_JSON_UTF8))
            .andExpect(status().isCreated())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
            .andDo(print());
    verify(this.postDao, only()).save(any());
  }

  @SneakyThrows
  @Test
  public void upload_201() {
    final MockMultipartFile mmf = new MockMultipartFile("file",
                                                        this.torrentFile.getName(),
                                                        SystemDefaultProperty.BITTORRENT_MIME,
                                                        new FileInputStream(this.torrentFile));
    this.mockMvc.perform(multipart("/post/1/torrent")
            .file(mmf))
            .andDo(print())
            .andExpect(status().isCreated());
  }
}
