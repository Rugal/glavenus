package ga.rugal.pt.springmvc.controller;

import java.net.URI;
import java.util.Optional;

import config.Constant;

import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.core.service.JwtService;
import ga.rugal.pt.core.service.UserService;
import ga.rugal.pt.openapi.api.UserApi;
import ga.rugal.pt.openapi.model.NewUserDto;
import ga.rugal.pt.openapi.model.UserDto;
import ga.rugal.pt.springmvc.mapper.UserMapper;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * User controller.
 *
 * @author Rugal Bernstein
 */
@Api(tags = "UserController")
@RestController
@Slf4j
public class UserController implements UserApi {

  @Autowired
  private UserService userService;

  @Autowired
  private JwtService jwtService;

  @Override
  public ResponseEntity<UserDto> create(final @RequestBody NewUserDto newUserDto) {
    if (this.userService.getDao().existsByEmail(newUserDto.getEmail())) {
      // Duplicate email
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    final User save = this.userService.getDao().save(UserMapper.INSTANCE.to(newUserDto));

    final UserDto from = UserMapper.INSTANCE.from(save);
    final URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().path("/{id}")
            .buildAndExpand(from.getUid()).toUri();

    return ResponseEntity
            .created(location)
            .body(from);
  }

  @Override
  public ResponseEntity<String> loginByUid(final @PathVariable(Constant.UID) Integer uid,
                                           final @RequestHeader(Constant.P) String password) {
    final Optional<User> optional = this.userService.getDao().findById(uid);
    // Return 404 if no user found
    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    // Return 401 if unauthenticated
    if (!this.userService.authenticate(uid, password)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    // Create jwt
    final String jwt = this.jwtService.encrypt(this.userService.getDao().findById(uid).get());
    final URI location = ServletUriComponentsBuilder
            .fromCurrentRequest().build().toUri();
    return ResponseEntity
            .created(location)
            .body(jwt);
  }

  @Override
  public ResponseEntity<String> loginByEmail(final @PathVariable(Constant.EMAIL) String email,
                                             final @RequestHeader(Constant.P) String password) {
    final Optional<User> optional = this.userService.getDao().findByEmail(email);
    if (optional.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    return this.loginByUid(optional.get().getUid(), password);
  }
}
