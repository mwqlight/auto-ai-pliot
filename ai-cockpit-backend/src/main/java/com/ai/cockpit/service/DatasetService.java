package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.DatasetRequest;
import com.ai.cockpit.dto.response.DatasetResponse;

import java.math.BigDecimal;
import java.util.List;

/**
 * 数据集服务接口
 */
public interface DatasetService {
    
    /**
     * 创建数据集
     */
    DatasetResponse createDataset(DatasetRequest request);
    
    /**
     * 更新数据集
     */
    DatasetResponse updateDataset(Long id, DatasetRequest request);
    
    /**
     * 根据ID删除数据集
     */
    void deleteDataset(Long id);
    
    /**
     * 根据ID查找数据集
     */
    DatasetResponse findById(Long id);
    
    /**
     * 根据数据集名称查找数据集
     */
    DatasetResponse findByName(String name);
    
    /**
     * 获取所有数据集列表
     */
    List<DatasetResponse> findAllDatasets();
    
    /**
     * 根据类型查找数据集列表
     */
    List<DatasetResponse> findByType(String type);
    
    /**
     * 根据状态查找数据集列表
     */
    List<DatasetResponse> findByStatus(Integer status);
    
    /**
     * 根据创建者ID查找数据集列表
     */
    List<DatasetResponse> findByCreatorId(Long creatorId);
    
    /**
     * 根据数据来源查找数据集列表
     */
    List<DatasetResponse> findBySource(String source);
    
    /**
     * 根据标签查找数据集列表
     */
    List<DatasetResponse> findByTagsContaining(String tag);
    
    /**
     * 根据名称或描述模糊查询数据集列表
     */
    List<DatasetResponse> searchByNameOrDescription(String keyword);
    
    /**
     * 更新数据集状态
     */
    void updateStatus(Long id, Integer status);
    
    /**
     * 更新数据集统计信息
     */
    void updateStatistics(Long id, String statistics);
    
    /**
     * 更新数据集标注进度
     */
    void updateAnnotationProgress(Long id, BigDecimal progress);
    
    /**
     * 获取公开数据集列表
     */
    List<DatasetResponse> findPublicDatasets();
    
    /**
     * 统计数据集数量
     */
    long countDatasets();
    
    /**
     * 统计不同类型数据集数量
     */
    long countByType(String type);
    
    /**
     * 统计不同状态数据集数量
     */
    long countByStatus(Integer status);
}