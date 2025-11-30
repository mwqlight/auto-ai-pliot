package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.AITechnologySolutionRequest;
import com.ai.cockpit.dto.response.AITechnologySolutionResponse;
import com.ai.cockpit.entity.AITechnologySolution;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * AI技术方案Mapper接口
 */
@Mapper(componentModel = "spring")
public interface AITechnologySolutionMapper {
    
    AITechnologySolutionMapper INSTANCE = Mappers.getMapper(AITechnologySolutionMapper.class);
    
    /**
     * 将请求DTO转换为实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "publishedTime", ignore = true)
    @Mapping(target = "usageCount", ignore = true)
    @Mapping(target = "rating", ignore = true)
    AITechnologySolution toEntity(AITechnologySolutionRequest request);
    
    /**
     * 将实体转换为响应DTO
     */
    @Mapping(source = "creatorId", target = "creatorId")
    AITechnologySolutionResponse toResponse(AITechnologySolution entity);
    
    /**
     * 更新实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "publishedTime", ignore = true)
    @Mapping(target = "usageCount", ignore = true)
    @Mapping(target = "rating", ignore = true)
    void updateEntityFromRequest(AITechnologySolutionRequest request, @MappingTarget AITechnologySolution entity);
}