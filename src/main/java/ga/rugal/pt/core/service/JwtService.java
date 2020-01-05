package ga.rugal.pt.core.service;

import java.util.Optional;

import ga.rugal.pt.core.entity.User;

import io.jsonwebtoken.Claims;

/**
 * Interface for Json Web Token service.
 *
 * @author Rugal Bernstein
 */
public interface JwtService {

  /**
   * Decrypt & verify given JWT against application secret and algorithm signature.
   *
   * @param jwt
   *
   * @return
   */
  Optional<Claims> decrypt(String jwt);

  /**
   * Encrypt user object into JWT.
   *
   * @param user
   *
   * @return
   */
  String encrypt(User user);
}
