package ga.rugal.pt.springmvc.controller;

import ga.rugal.IntegrationTestBase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author rugal
 */
@ContextConfiguration(classes = config.SpringMvcApplicationContext.class)
@Disabled
@SpringBootTest
@WebAppConfiguration
public abstract class ControllerIntegrationTestBase extends IntegrationTestBase {

  @Autowired
  public WebApplicationContext wac;

  public MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }
}
