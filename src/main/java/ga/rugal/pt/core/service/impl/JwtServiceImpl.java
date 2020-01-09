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
import org.springframework.stereotype.Service;

/**
 * Json Web Token service implementation.
 *
 * @author Rugal Bernstein
 */
@Service
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
  public Optional<Claims> decrypt(final String jwt) {
    try {
      // we can safely trust the JWT
      return Optional.of(Jwts.parser()
              .setSigningKey(this.key)
              .parseClaimsJws(jwt).getBody());
    } catch (final JwtException ex) {
      LOG.error("Invalid signature for JWS [{}]", jwt);
    }
    return Optional.empty();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(final String jwt) {
    // 1. Valid signature
    final Optional<Claims> decrypt = this.decrypt(jwt);
    if (decrypt.isEmpty()) {
      return false;
    }
    return this.isValid(decrypt.get());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValid(final Claims claims) {
    // 2. Valid expiration
    return claims.getExpiration().compareTo(new Date()) > 0
           // 3. Valid subject & issuer
           && claims.getSubject().equals(Constant.SUBJECT)
           && claims.getIssuer().equals(Constant.ISSUER);
  }
}
