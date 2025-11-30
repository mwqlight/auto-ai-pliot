package com.ai.cockpit.mapper;

import com.ai.cockpit.dto.request.TrainingTaskRequest;
import com.ai.cockpit.dto.response.TrainingTaskResponse;
import com.ai.cockpit.entity.TrainingTask;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 训练任务对象映射器
 */
@Mapper(componentModel = "spring")
public interface TrainingTaskMapper {
    
    TrainingTaskMapper INSTANCE = Mappers.getMapper(TrainingTaskMapper.class);
    
    /**
     * 请求DTO转换为实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "progress", ignore = true)
    @Mapping(target = "currentEpoch", ignore = true)
    @Mapping(target = "metrics", ignore = true)
    @Mapping(target = "logPath", ignore = true)
    @Mapping(target = "outputModelPath", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "estimatedEndTime", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "memoryUsage", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "creatorName", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    TrainingTask toEntity(TrainingTaskRequest request);
    
    /**
     * 实体转换为响应DTO
     */
    TrainingTaskResponse toResponse(TrainingTask task);
    
    /**
     * 更新实体
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "progress", ignore = true)
    @Mapping(target = "currentEpoch", ignore = true)
    @Mapping(target = "metrics", ignore = true)
    @Mapping(target = "logPath", ignore = true)
    @Mapping(target = "outputModelPath", ignore = true)
    @Mapping(target = "startTime", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "estimatedEndTime", ignore = true)
    @Mapping(target = "duration", ignore = true)
    @Mapping(target = "memoryUsage", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "creatorId", ignore = true)
    @Mapping(target = "creatorName", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    void updateEntity(TrainingTaskRequest request, @MappingTarget TrainingTask task);
    
    /**
     * 实体列表转换为响应DTO列表
     */
    List<TrainingTaskResponse> toResponseList(List<TrainingTask> tasks);
}