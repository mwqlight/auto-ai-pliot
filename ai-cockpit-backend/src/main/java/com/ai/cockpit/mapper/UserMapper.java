package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.UserRequest;
import com.ai.cockpit.dto.response.UserResponse;
import com.ai.cockpit.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 用户映射器
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * UserRequest 转 User
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    User toEntity(UserRequest request);

    /**
     * User 转 UserResponse
     */
    UserResponse toResponse(User user);

    /**
     * List<User> 转 List<UserResponse>
     */
    List<UserResponse> toResponseList(List<User> users);

    /**
     * 更新 User 实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "lastLoginTime", ignore = true)
    void updateEntity(UserRequest request, @MappingTarget User user);
}