package ga.rugal.pt.core.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import javax.crypto.SecretKey;

import config.Constant;

import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * Json Web Token service implementation.<BR>
 * TODO: This service is currently not in use.
 *
 * @author Rugal Bernstein
 */
@Slf4j
public class JwtServiceImpl implements JwtService {

  @Value("${application.jwt.expiration:86400}")
  private Integer expiration;

  @Autowired
  private SecretKey key;

  /**
   * Calculate expiration date by adding [application.jwt.expiration + now].
   *
   * @return
   */
  private Date getExpiration(final int exp) {
    final Calendar c = Calendar.getInstance();
    c.setTime(new Date());
    c.add(Calendar.SECOND, exp);
    return c.getTime();
  }

  /**
   * Put user information into a Map so JJWT can read it.
   *
   * @param user database user object
   *
   * @return
   */
  private Map<String, Object> getUserMap(final User user) {
    return Map.of(Constant.UID, user.getUid());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String encrypt(final User user) {
    return Jwts.builder()
            .setSubject(Constant.SUBJECT)
            .setIssuer(Constant.ISSUER)
            .setExpiration(this.getExpiration(this.expiration))
            .signWith(this.key)
            .addClaims(this.getUserMap(user))
            .compact();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Optional<Claims> decrypt(final String jws) {
    try {
      // we can safely trust the JWT
      return Optional.of(Jwts.parser()
              .setSigningKey(this.key)
              .parseClaimsJws(jws).getBody());
    } catch (final JwtException ex) {
      LOG.error("Invalid signature for JWS [{}]", jws);
    }
    return Optional.empty();
  }
}
