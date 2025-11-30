package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.RoleRequest;
import com.ai.cockpit.dto.response.RoleResponse;
import com.ai.cockpit.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 角色映射器
 */
@Mapper(componentModel = "spring", uses = {PermissionMapper.class})
public interface RoleMapper {
    
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    
    /**
     * 将RoleRequest转换为Role实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    Role toEntity(RoleRequest request);
    
    /**
     * 将Role实体转换为RoleResponse
     */
    @Mapping(target = "permissions", source = "permissions")
    @Mapping(target = "userCount", ignore = true)
    RoleResponse toResponse(Role role);
    
    /**
     * 将Role实体列表转换为RoleResponse列表
     */
    List<RoleResponse> toResponseList(List<Role> roles);
    
    /**
     * 更新Role实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "permissions", ignore = true)
    void updateEntity(RoleRequest request, @org.mapstruct.MappingTarget Role role);
}