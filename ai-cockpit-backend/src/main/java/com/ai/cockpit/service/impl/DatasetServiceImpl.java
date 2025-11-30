package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.DatasetRequest;
import com.ai.cockpit.dto.response.DatasetResponse;
import com.ai.cockpit.entity.Dataset;
import com.ai.cockpit.exception.BusinessException;
import com.ai.cockpit.mapper.DatasetMapper;
import com.ai.cockpit.repository.DatasetRepository;
import com.ai.cockpit.service.DatasetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据集服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatasetServiceImpl implements DatasetService {
    
    private final DatasetRepository datasetRepository;
    private final DatasetMapper datasetMapper;
    
    @Override
    @Transactional
    public DatasetResponse createDataset(DatasetRequest request) {
        // 检查数据集名称是否已存在
        if (datasetRepository.existsByName(request.getName())) {
            throw new BusinessException("数据集名称已存在");
        }
        
        Dataset dataset = datasetMapper.toEntity(request);
        // 设置默认值
        dataset.setStatus(0); // 未处理
        dataset.setIsPublic(false); // 默认不公开
        dataset.setAnnotationProgress(BigDecimal.ZERO);
        dataset.setCreateTime(LocalDateTime.now());
        dataset.setUpdateTime(LocalDateTime.now());
        // TODO: 设置创建者信息
        
        Dataset savedDataset = datasetRepository.save(dataset);
        log.info("创建数据集成功，数据集ID：{}", savedDataset.getId());
        
        return datasetMapper.toResponse(savedDataset);
    }
    
    @Override
    @Transactional
    public DatasetResponse updateDataset(Long id, DatasetRequest request) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new BusinessException("数据集不存在"));
        
        // 检查数据集名称是否已存在（排除当前数据集）
        if (!dataset.getName().equals(request.getName()) && 
            datasetRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BusinessException("数据集名称已存在");
        }
        
        datasetMapper.updateEntity(request, dataset);
        dataset.setUpdateTime(LocalDateTime.now());
        
        Dataset updatedDataset = datasetRepository.save(dataset);
        log.info("更新数据集成功，数据集ID：{}", id);
        
        return datasetMapper.toResponse(updatedDataset);
    }
    
    @Override
    @Transactional
    public void deleteDataset(Long id) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new BusinessException("数据集不存在"));
        
        // 检查数据集状态，处理中的数据集不能删除
        if (dataset.getStatus() == 1) {
            throw new BusinessException("处理中的数据集不能删除");
        }
        
        datasetRepository.deleteById(id);
        log.info("删除数据集成功，数据集ID：{}", id);
        
        // TODO: 删除相关的文件
    }
    
    @Override
    public DatasetResponse findById(Long id) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new BusinessException("数据集不存在"));
        return datasetMapper.toResponse(dataset);
    }
    
    @Override
    public DatasetResponse findByName(String name) {
        Dataset dataset = datasetRepository.findByName(name)
                .orElseThrow(() -> new BusinessException("数据集不存在"));
        return datasetMapper.toResponse(dataset);
    }
    
    @Override
    public List<DatasetResponse> findAllDatasets() {
        List<Dataset> datasets = datasetRepository.findAll();
        return datasetMapper.toResponseList(datasets);
    }
    
    @Override
    public List<DatasetResponse> findByType(String type) {
        List<Dataset> datasets = datasetRepository.findByType(type);
        return datasetMapper.toResponseList(datasets);
    }
    
    @Override
    public List<DatasetResponse> findByStatus(Integer status) {
        List<Dataset> datasets = datasetRepository.findByStatus(status);
        return datasetMapper.toResponseList(datasets);
    }
    
    @Override
    public List<DatasetResponse> findByCreatorId(Long creatorId) {
        List<Dataset> datasets = datasetRepository.findByCreatorId(creatorId);
        return datasetMapper.toResponseList(datasets);
    }
    
    @Override
    public List<DatasetResponse> findBySource(String source) {
        List<Dataset> datasets = datasetRepository.findBySource(source);
        return datasetMapper.toResponseList(datasets);
    }
    
    @Override
    public List<DatasetResponse> findByTagsContaining(String tag) {
        // 临时解决方案：暂时返回所有数据集，待修复复杂查询问题
        // List<Dataset> datasets = datasetRepository.findByTagsContaining(tag);
        
        List<Dataset> datasets = datasetRepository.findAll();
        return datasetMapper.toResponseList(datasets);
    }
    
    @Override
    public List<DatasetResponse> searchByNameOrDescription(String keyword) {
        // 临时解决方案：使用简单的名称模糊查询
        List<Dataset> datasets = datasetRepository.findByNameContaining(keyword);
        return datasetMapper.toResponseList(datasets);
    }
    
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new BusinessException("数据集不存在"));
        
        dataset.setStatus(status);
        dataset.setUpdateTime(LocalDateTime.now());
        
        datasetRepository.save(dataset);
        log.info("更新数据集状态，数据集ID：{}，新状态：{}", id, status);
    }
    
    @Override
    @Transactional
    public void updateStatistics(Long id, String statistics) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new BusinessException("数据集不存在"));
        
        dataset.setStatistics(statistics);
        dataset.setUpdateTime(LocalDateTime.now());
        
        datasetRepository.save(dataset);
        log.debug("更新数据集统计信息，数据集ID：{}", id);
    }
    
    @Override
    @Transactional
    public void updateAnnotationProgress(Long id, BigDecimal progress) {
        Dataset dataset = datasetRepository.findById(id)
                .orElseThrow(() -> new BusinessException("数据集不存在"));
        
        if (progress.compareTo(BigDecimal.ZERO) < 0 || progress.compareTo(BigDecimal.valueOf(100)) > 0) {
            throw new BusinessException("标注进度必须在0-100之间");
        }
        
        dataset.setAnnotationProgress(progress);
        dataset.setUpdateTime(LocalDateTime.now());
        
        datasetRepository.save(dataset);
        log.debug("更新数据集标注进度，数据集ID：{}，进度：{}%", id, progress);
    }
    
    @Override
    public List<DatasetResponse> findPublicDatasets() {
        List<Dataset> datasets = datasetRepository.findByIsPublicTrue();
        return datasetMapper.toResponseList(datasets);
    }
    
    @Override
    public long countDatasets() {
        return datasetRepository.count();
    }
    
    @Override
    public long countByType(String type) {
        return datasetRepository.countByType(type);
    }
    
    @Override
    public long countByStatus(Integer status) {
        // 临时解决方案：手动统计状态数据
        // return datasetRepository.countByStatus(status);
        
        List<Dataset> allDatasets = datasetRepository.findAll();
        return allDatasets.stream()
                .filter(dataset -> dataset.getStatus().equals(status))
                .count();
    }
}