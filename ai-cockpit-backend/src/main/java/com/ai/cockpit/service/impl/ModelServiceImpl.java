package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.ModelRequest;
import com.ai.cockpit.dto.response.ModelResponse;
import com.ai.cockpit.entity.Model;
import com.ai.cockpit.exception.BusinessException;
import com.ai.cockpit.mapper.ModelMapper;
import com.ai.cockpit.repository.ModelRepository;
import com.ai.cockpit.service.ModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 模型服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ModelServiceImpl implements ModelService {

    private final ModelRepository modelRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public ModelResponse createModel(ModelRequest request) {
        // 检查模型名称是否已存在
        if (modelRepository.existsByName(request.getName())) {
            throw new BusinessException(20001, "模型名称已存在");
        }
        
        // 转换DTO为实体
        Model model = modelMapper.toEntity(request);
        model.setCreatedTime(LocalDateTime.now());
        model.setUpdatedTime(LocalDateTime.now());
        
        // TODO: 设置创建者信息（从安全上下文获取）
        model.setCreatedBy(1L); // 临时设置
        
        Model savedModel = modelRepository.save(model);
        
        log.info("创建模型成功: {}", savedModel.getName());
        
        return modelMapper.toResponse(savedModel);
    }

    @Override
    @Transactional
    public ModelResponse updateModel(Long id, ModelRequest request) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(20002, "模型不存在"));
        
        // 检查模型名称是否已存在（排除当前模型）
        if (modelRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BusinessException(20001, "模型名称已存在");
        }
        
        // 更新模型信息
        modelMapper.updateEntity(request, model);
        model.setUpdatedTime(LocalDateTime.now());
        
        Model updatedModel = modelRepository.save(model);
        
        log.info("更新模型成功: {}", updatedModel.getName());
        
        return modelMapper.toResponse(updatedModel);
    }

    @Override
    @Transactional
    public void deleteModel(Long id) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(20002, "模型不存在"));
        
        // 检查模型是否正在使用（TODO: 检查部署和训练任务）
        
        modelRepository.delete(model);
        log.info("删除模型成功: {}", model.getName());
    }

    @Override
    public ModelResponse findById(Long id) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(20002, "模型不存在"));
        return modelMapper.toResponse(model);
    }

    @Override
    public ModelResponse findByName(String name) {
        Model model = modelRepository.findByName(name)
                .orElseThrow(() -> new BusinessException(20002, "模型不存在"));
        return modelMapper.toResponse(model);
    }

    @Override
    public List<ModelResponse> findAllModels() {
        List<Model> models = modelRepository.findAll();
        return modelMapper.toResponseList(models);
    }

    @Override
    public List<ModelResponse> findByType(String type) {
        List<Model> models = modelRepository.findByType(type);
        return modelMapper.toResponseList(models);
    }

    @Override
    public List<ModelResponse> findByStatus(Integer status) {
        List<Model> models = modelRepository.findByStatus(status);
        return modelMapper.toResponseList(models);
    }

    @Override
    public List<ModelResponse> findByCreatorId(Long creatorId) {
        List<Model> models = modelRepository.findByCreatedBy(creatorId);
        return modelMapper.toResponseList(models);
    }

    @Override
    @Transactional
    public void toggleModelPublicStatus(Long id, boolean isPublic) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(20002, "模型不存在"));
        
        // model.setIsPublic(isPublic); // 该字段已删除
        model.setUpdatedTime(LocalDateTime.now());
        
        modelRepository.save(model);
        log.info("{}模型: {}", isPublic ? "公开" : "取消公开", model.getName());
    }

    @Override
    @Transactional
    public void updateModelStatus(Long id, Integer status) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(20002, "模型不存在"));
        
        model.setStatus(status);
        model.setUpdatedTime(LocalDateTime.now());
        
        modelRepository.save(model);
        log.info("更新模型状态: {}, 状态: {}", model.getName(), status);
    }

    @Override
    @Transactional
    public void updateModelPerformance(Long id, String performanceMetrics) {
        Model model = modelRepository.findById(id)
                .orElseThrow(() -> new BusinessException(20002, "模型不存在"));
        
        // model.setPerformanceMetrics(performanceMetrics); // 该字段已删除
        model.setUpdatedTime(LocalDateTime.now());
        
        modelRepository.save(model);
        log.info("更新模型性能指标: {}", model.getName());
    }

    @Override
    public List<ModelResponse> searchModels(String keyword) {
        // 临时解决方案：使用简单的名称模糊查询
        List<Model> models = modelRepository.findByNameContaining(keyword);
        return modelMapper.toResponseList(models);
    }
}