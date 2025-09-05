package org.example.tttn.mapper;

import org.example.tttn.dto.*;
import org.example.tttn.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper cho entity User.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Chuyển đổi RegisterRequest thành User entity
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequest request);

    /**
     * Chuyển đổi User entity thành RegisterResponse
     */
    @Mapping(target = "userId", source = "id")
    RegisterResponse toRegisterResponse(User user);

    /**
     * Chuyển đổi User entity thành LoginResponse
     */
    @Mapping(target = "userId", source = "id")
    @Mapping(target = "fullName", ignore = true)
    @Mapping(target = "accessToken", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    LoginResponse toLoginResponse(User user);
}
