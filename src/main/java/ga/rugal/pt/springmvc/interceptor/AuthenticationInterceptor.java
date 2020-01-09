package ga.rugal.pt.springmvc.interceptor;

import java.io.IOException;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.Constant;

import ga.rugal.pt.core.service.JwtService;

import io.jsonwebtoken.Claims;
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
  private JwtService jwtService;

  /**
   * This method is just for generating a response with forbidden content.<BR>
   * May throw IOException inside because unable to get response body writer, but this version will
   * shelter it.
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
   * This interceptor do its jobs on most of business logic handlers.<BR>
   * Any request that needs authentication must include their {@link Constant#TOKEN} in request
   * header.<BR>
   * Example:<BR> {@code curl -H'Authorization: Bearer XXXX'}
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
      LOG.trace("Allow preflight request");
      //Allow preflight request
      return true;
    }

    final String authorization = request.getHeader(Constant.AUTHORIZATION);
    if (Objects.isNull(authorization) || authorization.isBlank()) {
      LOG.warn("Host [{}] try to access [{}] without Json Web Token",
               request.getRemoteAddr(),
               request.getRequestURI());
      return this.denyResponse(response);
    }

    final String[] split = authorization.split(" ", 2);
    if (split.length < 2 // Invalid Authorization header format
        || !this.jwtService.isValid(split[1])) { // Invalid jwt
      LOG.warn("Host [{}] try to access [{}] invalid Json Web Token",
               request.getRemoteAddr(),
               request.getRequestURI());
      return this.denyResponse(response);
    }

    final Claims claim = this.jwtService.decrypt(split[1]).get();
    final int uid = (Integer) claim.get(Constant.UID);
    request.setAttribute(Constant.UID, uid);
    LOG.debug("User [{}] @ Host [{}] try to access [{}]",
              uid,
              request.getRemoteAddr(),
              request.getRequestURI());
    return true;
  }
}
