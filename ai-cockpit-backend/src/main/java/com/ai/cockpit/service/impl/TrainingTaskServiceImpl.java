package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.TrainingTaskRequest;
import com.ai.cockpit.dto.response.TrainingTaskResponse;
import com.ai.cockpit.entity.TrainingTask;
import com.ai.cockpit.exception.BusinessException;
import com.ai.cockpit.mapper.TrainingTaskMapper;
import com.ai.cockpit.repository.TrainingTaskRepository;
import com.ai.cockpit.service.TrainingTaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 训练任务服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTaskServiceImpl implements TrainingTaskService {
    
    private final TrainingTaskRepository trainingTaskRepository;
    private final TrainingTaskMapper trainingTaskMapper;
    
    @Override
    @Transactional
    public TrainingTaskResponse createTask(TrainingTaskRequest request) {
        // 检查任务名称是否已存在
        if (trainingTaskRepository.existsByName(request.getName())) {
            throw new BusinessException("任务名称已存在");
        }
        
        TrainingTask task = trainingTaskMapper.toEntity(request);
        // 设置默认值
        task.setStatus(0); // 等待中
        task.setProgress(0);
        task.setCurrentEpoch(0);
        task.setCreateTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        // TODO: 设置创建者信息
        
        TrainingTask savedTask = trainingTaskRepository.save(task);
        log.info("创建训练任务成功，任务ID：{}", savedTask.getId());
        
        return trainingTaskMapper.toResponse(savedTask);
    }
    
    @Override
    @Transactional
    public TrainingTaskResponse updateTask(Long id, TrainingTaskRequest request) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        // 检查任务名称是否已存在（排除当前任务）
        if (!task.getName().equals(request.getName()) && 
            trainingTaskRepository.existsByNameAndIdNot(request.getName(), id)) {
            throw new BusinessException("任务名称已存在");
        }
        
        trainingTaskMapper.updateEntity(request, task);
        task.setUpdateTime(LocalDateTime.now());
        
        TrainingTask updatedTask = trainingTaskRepository.save(task);
        log.info("更新训练任务成功，任务ID：{}", id);
        
        return trainingTaskMapper.toResponse(updatedTask);
    }
    
    @Override
    @Transactional
    public void deleteTask(Long id) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        // 检查任务状态，运行中的任务不能删除
        if (task.getStatus() == 1 || task.getStatus() == 2) {
            throw new BusinessException("运行中的任务不能删除");
        }
        
        trainingTaskRepository.deleteById(id);
        log.info("删除训练任务成功，任务ID：{}", id);
    }
    
    @Override
    public TrainingTaskResponse findById(Long id) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        return trainingTaskMapper.toResponse(task);
    }
    
    @Override
    public TrainingTaskResponse findByName(String name) {
        TrainingTask task = trainingTaskRepository.findByName(name)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        return trainingTaskMapper.toResponse(task);
    }
    
    @Override
    public List<TrainingTaskResponse> findAllTasks() {
        List<TrainingTask> tasks = trainingTaskRepository.findAll();
        return trainingTaskMapper.toResponseList(tasks);
    }
    
    @Override
    public List<TrainingTaskResponse> findByStatus(Integer status) {
        List<TrainingTask> tasks = trainingTaskRepository.findByStatus(status);
        return trainingTaskMapper.toResponseList(tasks);
    }
    
    @Override
    public List<TrainingTaskResponse> findByModelId(Long modelId) {
        List<TrainingTask> tasks = trainingTaskRepository.findByModelId(modelId);
        return trainingTaskMapper.toResponseList(tasks);
    }
    
    @Override
    public List<TrainingTaskResponse> findByDatasetId(Long datasetId) {
        List<TrainingTask> tasks = trainingTaskRepository.findByDatasetId(datasetId);
        return trainingTaskMapper.toResponseList(tasks);
    }
    
    @Override
    public List<TrainingTaskResponse> findByCreatorId(Long creatorId) {
        List<TrainingTask> tasks = trainingTaskRepository.findByCreatorId(creatorId);
        return trainingTaskMapper.toResponseList(tasks);
    }
    
    @Override
    @Transactional
    public void startTask(Long id) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        if (task.getStatus() != 0) {
            throw new BusinessException("只有等待中的任务可以启动");
        }
        
        task.setStatus(1); // 运行中
        task.setStartTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        
        trainingTaskRepository.save(task);
        log.info("启动训练任务成功，任务ID：{}", id);
        
        // TODO: 调用实际的训练服务
    }
    
    @Override
    @Transactional
    public void stopTask(Long id) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        if (task.getStatus() != 1 && task.getStatus() != 2) {
            throw new BusinessException("只有运行中的任务可以停止");
        }
        
        task.setStatus(3); // 已停止
        task.setEndTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        
        trainingTaskRepository.save(task);
        log.info("停止训练任务成功，任务ID：{}", id);
        
        // TODO: 调用实际的停止训练服务
    }
    
    @Override
    @Transactional
    public void cancelTask(Long id) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        if (task.getStatus() == 2 || task.getStatus() == 4) {
            throw new BusinessException("已完成或已取消的任务不能再次取消");
        }
        
        task.setStatus(4); // 已取消
        task.setEndTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        
        trainingTaskRepository.save(task);
        log.info("取消训练任务成功，任务ID：{}", id);
    }
    
    @Override
    @Transactional
    public void updateProgress(Long id, Integer progress, Integer currentEpoch) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        if (task.getStatus() != 1 && task.getStatus() != 2) {
            throw new BusinessException("只有运行中的任务可以更新进度");
        }
        
        task.setProgress(progress);
        task.setCurrentEpoch(currentEpoch);
        task.setUpdateTime(LocalDateTime.now());
        
        trainingTaskRepository.save(task);
        log.debug("更新训练任务进度，任务ID：{}，进度：{}%，当前epoch：{}", id, progress, currentEpoch);
    }
    
    @Override
    @Transactional
    public void updateMetrics(Long id, String metrics) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        task.setMetrics(metrics);
        task.setUpdateTime(LocalDateTime.now());
        
        trainingTaskRepository.save(task);
        log.debug("更新训练任务指标，任务ID：{}", id);
    }
    
    @Override
    @Transactional
    public void updateStatus(Long id, Integer status) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        task.setStatus(status);
        task.setUpdateTime(LocalDateTime.now());
        
        if (status == 2) { // 已完成
            task.setEndTime(LocalDateTime.now());
        }
        
        trainingTaskRepository.save(task);
        log.info("更新训练任务状态，任务ID：{}，新状态：{}", id, status);
    }
    
    @Override
    @Transactional
    public void recordError(Long id, String errorMessage) {
        TrainingTask task = trainingTaskRepository.findById(id)
                .orElseThrow(() -> new BusinessException("训练任务不存在"));
        
        task.setStatus(3); // 失败
        task.setErrorMessage(errorMessage);
        task.setEndTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        
        trainingTaskRepository.save(task);
        log.error("记录训练任务错误，任务ID：{}，错误信息：{}", id, errorMessage);
    }
}