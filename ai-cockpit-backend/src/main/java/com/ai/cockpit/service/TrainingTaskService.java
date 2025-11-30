package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.TrainingTaskRequest;
import com.ai.cockpit.dto.response.TrainingTaskResponse;

import java.util.List;

/**
 * 训练任务服务接口
 */
public interface TrainingTaskService {
    
    /**
     * 创建训练任务
     */
    TrainingTaskResponse createTask(TrainingTaskRequest request);
    
    /**
     * 更新训练任务
     */
    TrainingTaskResponse updateTask(Long id, TrainingTaskRequest request);
    
    /**
     * 根据ID删除训练任务
     */
    void deleteTask(Long id);
    
    /**
     * 根据ID查找训练任务
     */
    TrainingTaskResponse findById(Long id);
    
    /**
     * 根据任务名称查找训练任务
     */
    TrainingTaskResponse findByName(String name);
    
    /**
     * 获取所有训练任务列表
     */
    List<TrainingTaskResponse> findAllTasks();
    
    /**
     * 根据状态查找训练任务列表
     */
    List<TrainingTaskResponse> findByStatus(Integer status);
    
    /**
     * 根据模型ID查找训练任务列表
     */
    List<TrainingTaskResponse> findByModelId(Long modelId);
    
    /**
     * 根据数据集ID查找训练任务列表
     */
    List<TrainingTaskResponse> findByDatasetId(Long datasetId);
    
    /**
     * 根据创建者ID查找训练任务列表
     */
    List<TrainingTaskResponse> findByCreatorId(Long creatorId);
    
    /**
     * 启动训练任务
     */
    void startTask(Long id);
    
    /**
     * 停止训练任务
     */
    void stopTask(Long id);
    
    /**
     * 取消训练任务
     */
    void cancelTask(Long id);
    
    /**
     * 更新训练进度
     */
    void updateProgress(Long id, Integer progress, Integer currentEpoch);
    
    /**
     * 更新训练指标
     */
    void updateMetrics(Long id, String metrics);
    
    /**
     * 更新训练状态
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 记录训练错误
     */
    void recordError(Long id, String errorMessage);
}