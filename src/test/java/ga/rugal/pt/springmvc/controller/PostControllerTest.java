package ga.rugal.pt.springmvc.controller;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import config.SystemDefaultProperty;

import ga.rugal.pt.core.dao.PostDao;
import ga.rugal.pt.core.entity.Post;
import ga.rugal.pt.core.service.PostService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
  private Post post;
  
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
  }
  
  @Test
  public void upload_201() throws Exception {
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
