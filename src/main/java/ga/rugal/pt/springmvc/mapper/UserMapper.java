package ga.rugal.pt.springmvc.mapper;

import ga.rugal.pt.core.entity.User;
import ga.rugal.pt.openapi.model.NewUserDto;
import ga.rugal.pt.openapi.model.UserDto;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * The Data Mapper For Course.
 *
 * @author Rugal Bernstein
 */
@Mapper(config = CentralConfig.class)
@SuppressFBWarnings("UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD")
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  User to(NewUserDto newUserDto);

  User to(UserDto userDto);

  UserDto from(User user);
}
