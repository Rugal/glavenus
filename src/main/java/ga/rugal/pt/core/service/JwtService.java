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
   * @return optional claims object
   */
  Optional<Claims> decrypt(String jwt);

  /**
   * Encrypt user object into JWT.
   *
   * @param user
   *
   * @return JWT string
   */
  String encrypt(User user);

  /**
   * Validate against given jwt to see if it has<BR>
   * 1. Valid signature<BR>
   * 2. Within expiration<BR>
   * 3. Valid subject & issuer
   *
   * @param jwt
   *
   * @return true if JWT is well formatted and signature valid
   */
  boolean isValid(String jwt);

  /**
   * Validate against given jwt claim object to see if it has<BR>
   * 1. Within expiration<BR>
   * 2. Valid subject & issuer
   *
   * @param claims
   *
   * @return true if JWT is well formatted and signature valid
   */
  boolean isValid(Claims claims);
}
