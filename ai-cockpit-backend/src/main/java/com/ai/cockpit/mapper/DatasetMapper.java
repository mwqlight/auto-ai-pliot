package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.DatasetRequest;
import com.ai.cockpit.dto.response.DatasetResponse;
import com.ai.cockpit.entity.Dataset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 数据集对象映射器
 */
@Mapper(componentModel = "spring")
public interface DatasetMapper {
    
    DatasetMapper INSTANCE = Mappers.getMapper(DatasetMapper.class);
    
    /**
     * 请求DTO转换为实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "creatorName", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    Dataset toEntity(DatasetRequest request);
    
    /**
     * 实体转换为响应DTO
     */
    DatasetResponse toResponse(Dataset dataset);
    
    /**
     * 更新实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "creatorName", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    void updateEntity(DatasetRequest request, @MappingTarget Dataset dataset);
    
    /**
     * 实体列表转换为响应DTO列表
     */
    List<DatasetResponse> toResponseList(List<Dataset> datasets);
}