package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.PermissionRequest;
import com.ai.cockpit.dto.response.PermissionResponse;
import com.ai.cockpit.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 权限映射器
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper {
    
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);
    
    /**
     * 将PermissionRequest转换为Permission实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Permission toEntity(PermissionRequest request);
    
    /**
     * 将Permission实体转换为PermissionResponse
     */
    PermissionResponse toResponse(Permission permission);
    
    /**
     * 将Permission实体列表转换为PermissionResponse列表
     */
    List<PermissionResponse> toResponseList(List<Permission> permissions);
    
    /**
     * 更新Permission实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void updateEntity(PermissionRequest request, @org.mapstruct.MappingTarget Permission permission);
}