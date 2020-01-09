package ga.rugal.pt.springmvc.interceptor;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.Constant;

import ga.rugal.pt.core.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * An authentication interceptor that authenticate any matched request by credential.
 *
 * @author Rugal Bernstein
 */
@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {

  @Autowired
  private UserService userService;

  /**
   * This method used put authentication. If you need to check with database, please modify code.
   *
   * @param uid      user ID
   * @param password user password
   *
   * @return true if this user and credential meet requirement, otherwise return false
   */
  private boolean isAuthenticated(final String uid, final String password) {
    boolean isAuthenticated = false;
    try {
      isAuthenticated = this.userService.authenticate(Integer.parseInt(uid), password);
    } catch (final NumberFormatException e) {
      LOG.error(e.getMessage());
    }

    return isAuthenticated;
  }

  /**
   * This method is just for generating a response with forbidden content.<BR>
   * May throw IOException inside because unable to get response body writer, but this version will
   * shelter it.
   *
   *
   * @param response The response corresponding to the request.
   */
  private boolean denyResponse(final HttpServletResponse response) {
    try {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.getWriter().flush();
    } catch (final IOException e) {
      LOG.error("Unable to get response writer", e);
    }
    return false;
  }

  /**
   * This interceptor do its jobs on all business logic handlers.<BR>
   * Any request that needs authentication must include their {@link Constant#UID} and
   * {@link Constant#PASSWORD} in plain text in request header.<BR>
   * Example:<BR> {@code curl -H'uid:1' -H'password:123456'}
   *
   * @param request  The request that has id and credential information in header
   * @param response The response to user
   * @param handler  Request handler
   *
   * @return true if id and credential match information inside DB, otherwise return false.
   *
   * @throws Exception when interceptor unable to handle
   */
  @Override
  public boolean preHandle(final HttpServletRequest request,
                           final HttpServletResponse response,
                           final Object handler) throws Exception {
    if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
      LOG.debug("Allow preflight request");
      //Allow preflight request
      return true;
    }
    final String uid = request.getHeader(Constant.UID);
    final String password = request.getHeader(Constant.PASSWORD);
    if (this.isAuthenticated(uid, password)) {
      //User authenticated, move forward
      return true;
    }
    LOG.warn("User [{}] with password [{}] unable to access [{}] from host [{}]",
             uid,
             password,
             request.getRequestURI(),
             request.getRemoteAddr());
    // Flush response
    return this.denyResponse(response);
  }
}
