package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.BusinessApplicationRequest;
import com.ai.cockpit.dto.response.BusinessApplicationResponse;
import com.ai.cockpit.entity.BusinessApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * 业务应用市场Mapper接口
 */
@Mapper(componentModel = "spring")
public interface BusinessApplicationMapper {
    
    BusinessApplicationMapper INSTANCE = Mappers.getMapper(BusinessApplicationMapper.class);
    
    /**
     * 将请求DTO转换为实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "usageCount", ignore = true)
    @Mapping(target = "downloadCount", ignore = true)
    @Mapping(target = "favoriteCount", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "isPopular", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "publishedTime", ignore = true)
    @Mapping(target = "lastUpdaterId", ignore = true)
    BusinessApplication toEntity(BusinessApplicationRequest request);
    
    /**
     * 将实体转换为响应DTO
     */
    @Mapping(source = "creatorId", target = "creatorId")
    BusinessApplicationResponse toResponse(BusinessApplication entity);
    
    /**
     * 使用请求DTO更新实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "rating", ignore = true)
    @Mapping(target = "usageCount", ignore = true)
    @Mapping(target = "downloadCount", ignore = true)
    @Mapping(target = "favoriteCount", ignore = true)
    @Mapping(target = "reviewCount", ignore = true)
    @Mapping(target = "isPopular", ignore = true)
    @Mapping(target = "creator", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    @Mapping(target = "publishedTime", ignore = true)
    @Mapping(target = "lastUpdaterId", ignore = true)
    void updateEntityFromRequest(BusinessApplicationRequest request, @MappingTarget BusinessApplication entity);
}