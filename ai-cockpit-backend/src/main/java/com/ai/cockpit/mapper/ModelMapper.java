package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.ModelRequest;
import com.ai.cockpit.dto.response.ModelResponse;
import com.ai.cockpit.entity.Model;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 模型对象映射器
 */
@Mapper(componentModel = "spring")
public interface ModelMapper {
    
    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);
    
    /**
     * 请求DTO转换为实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    Model toEntity(ModelRequest request);
    
    /**
     * 实体转换为响应DTO
     */
    ModelResponse toResponse(Model model);
    
    /**
     * 更新实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    void updateEntity(ModelRequest request, @MappingTarget Model model);
    
    /**
     * 实体列表转换为响应DTO列表
     */
    List<ModelResponse> toResponseList(List<Model> models);
}