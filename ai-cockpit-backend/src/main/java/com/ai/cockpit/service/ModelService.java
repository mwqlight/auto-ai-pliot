package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.ModelRequest;
import com.ai.cockpit.dto.response.ModelResponse;

import java.util.List;

/**
 * 模型服务接口
 */
public interface ModelService {
    
    /**
     * 创建模型
     */
    ModelResponse createModel(ModelRequest request);
    
    /**
     * 更新模型
     */
    ModelResponse updateModel(Long id, ModelRequest request);
    
    /**
     * 根据ID删除模型
     */
    void deleteModel(Long id);
    
    /**
     * 根据ID查找模型
     */
    ModelResponse findById(Long id);
    
    /**
     * 根据模型名称查找模型
     */
    ModelResponse findByName(String name);
    
    /**
     * 获取所有模型列表
     */
    List<ModelResponse> findAllModels();
    
    /**
     * 根据类型查找模型列表
     */
    List<ModelResponse> findByType(String type);
    
    /**
     * 根据状态查找模型列表
     */
    List<ModelResponse> findByStatus(Integer status);
    
    /**
     * 根据创建者ID查找模型列表
     */
    List<ModelResponse> findByCreatorId(Long creatorId);
    
    /**
     * 切换模型公开状态
     */
    void toggleModelPublicStatus(Long id, boolean isPublic);
    
    /**
     * 更新模型状态
     */
    void updateModelStatus(Long id, Integer status);
    
    /**
     * 更新模型性能指标
     */
    void updateModelPerformance(Long id, String performanceMetrics);
    
    /**
     * 搜索模型
     */
    List<ModelResponse> searchModels(String keyword);
}