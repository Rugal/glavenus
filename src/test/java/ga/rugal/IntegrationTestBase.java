package ga.rugal;

import config.ApplicationContext;
import config.SpringMvcApplicationContext;
import config.TestApplicationContext;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This is a base test class for any integration test in testing stage<BR>
 * With this class, we don't need to import configuration for each test class repeatedly.
 *
 * @author Rugal Bernstein
 */
@ContextConfiguration(classes = {TestApplicationContext.class,
                                 ApplicationContext.class,
                                 SpringMvcApplicationContext.class,})
@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
public abstract class IntegrationTestBase {
}
