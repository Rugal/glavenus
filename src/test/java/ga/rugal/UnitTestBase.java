package ga.rugal;

import config.TestApplicationContext;
import config.UnitTestApplicationContext;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * This is a base test class for any unit test in testing stage<BR>
 * with the assistance of this test base class, there is no need to redeploy web server over and
 * over again, which is too time wastage.<BR>
 * Any classes that extends this class could have the capability to test without web server
 * deployment
 *
 * @author Rugal Bernstein
 */
@ContextConfiguration(classes = {TestApplicationContext.class, UnitTestApplicationContext.class})
@Disabled
@ExtendWith(SpringExtension.class)
public abstract class UnitTestBase {
}
