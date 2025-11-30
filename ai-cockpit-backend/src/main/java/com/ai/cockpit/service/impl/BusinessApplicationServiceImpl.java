package com.ai.cockpit.service.impl;

import com.ai.cockpit.dto.request.BusinessApplicationRequest;
import com.ai.cockpit.dto.response.BusinessApplicationResponse;
import com.ai.cockpit.entity.BusinessApplication;
import com.ai.cockpit.entity.User;
import com.ai.cockpit.exception.BusinessException;
import com.ai.cockpit.mapper.BusinessApplicationMapper;
import com.ai.cockpit.repository.BusinessApplicationRepository;
import com.ai.cockpit.repository.UserRepository;
import com.ai.cockpit.service.BusinessApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务应用市场Service实现类
 */
@Service
@RequiredArgsConstructor
public class BusinessApplicationServiceImpl implements BusinessApplicationService {
    
    private final BusinessApplicationRepository applicationRepository;
    private final UserRepository userRepository;
    private final BusinessApplicationMapper applicationMapper;
    
    @Override
    @Transactional
    public BusinessApplicationResponse createApplication(BusinessApplicationRequest request) {
        // 检查应用代码是否已存在
        if (applicationRepository.existsByCode(request.getCode())) {
            throw new BusinessException("应用代码已存在：" + request.getCode());
        }
        
        // 获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在：" + username));
        
        // 创建应用
        BusinessApplication application = applicationMapper.toEntity(request);
        application.setCreator(creator);
        application.setCreatorId(creator.getId());
        application.setCreatedTime(LocalDateTime.now());
        application.setUpdatedTime(LocalDateTime.now());
        
        // 设置默认值
        application.setRating(BigDecimal.ZERO);
        application.setUsageCount(0);
        application.setDownloadCount(0);
        application.setFavoriteCount(0);
        application.setReviewCount(0);
        application.setIsPopular(false);
        
        BusinessApplication savedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(savedApplication);
    }
    
    @Override
    @Transactional
    public BusinessApplicationResponse updateApplication(Long id, BusinessApplicationRequest request) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        // 检查应用代码是否被其他应用使用
        if (!application.getCode().equals(request.getCode()) && 
            applicationRepository.existsByCode(request.getCode())) {
            throw new BusinessException("应用代码已存在：" + request.getCode());
        }
        
        // 获取当前登录用户
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User updater = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在：" + username));
        
        applicationMapper.updateEntityFromRequest(request, application);
        application.setLastUpdaterId(updater.getId());
        application.setUpdatedTime(LocalDateTime.now());
        
        BusinessApplication updatedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(updatedApplication);
    }
    
    @Override
    @Transactional
    public void deleteApplication(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        // 检查权限（只有创建者或管理员可以删除）
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new BusinessException("用户不存在：" + username));
        
        if (!application.getCreatorId().equals(currentUser.getId()) && 
            !currentUser.getRoles().stream().anyMatch(role -> "ADMIN".equals(role))) {
            throw new BusinessException("无权删除此应用");
        }
        
        applicationRepository.delete(application);
    }
    
    @Override
    public BusinessApplicationResponse getApplicationById(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        return applicationMapper.toResponse(application);
    }
    
    @Override
    public BusinessApplicationResponse getApplicationByCode(String code) {
        BusinessApplication application = applicationRepository.findByCode(code)
                .orElseThrow(() -> new BusinessException("应用不存在，代码：" + code));
        return applicationMapper.toResponse(application);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getAllApplications(Pageable pageable) {
        return applicationRepository.findAll(pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> searchApplicationsByName(String name, Pageable pageable) {
        return applicationRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getApplicationsByCategory(String category, Pageable pageable) {
        return applicationRepository.findByCategory(category, pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getApplicationsByCategoryAndSubCategory(String category, String subCategory, Pageable pageable) {
        return applicationRepository.findByCategoryAndSubCategory(category, subCategory, pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getFreeApplications(Pageable pageable) {
        return applicationRepository.findByIsFreeTrue(pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getRecommendedApplications(Pageable pageable) {
        return applicationRepository.findByIsRecommendedTrue(pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getPopularApplications(Pageable pageable) {
        return applicationRepository.findByIsPopularTrue(pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getPublicApplications(Pageable pageable) {
        return applicationRepository.findByIsPublicTrue(pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getApplicationsByStatus(Integer status, Pageable pageable) {
        return applicationRepository.findByStatus(status, pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getApplicationsByCreator(Long creatorId, Pageable pageable) {
        return applicationRepository.findByCreatorId(creatorId, pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> getApplicationsByTag(String tag, Pageable pageable) {
        // 临时解决方案：暂时返回所有应用，待修复复杂查询问题
        // return applicationRepository.findByTag(tag, pageable)
        //         .map(applicationMapper::toResponse);
        
        return applicationRepository.findAll(pageable)
                .map(applicationMapper::toResponse);
    }
    
    @Override
    public Page<BusinessApplicationResponse> searchApplications(String name, String category, Boolean isFree, Integer status, Pageable pageable) {
        // TODO: 修复复杂查询问题后重新启用
        // return applicationRepository.findByMultipleConditions(name, category, isFree, status, pageable)
        //         .map(applicationMapper::toResponse);
        
        // 临时解决方案：使用简单查询
        if (name != null && !name.trim().isEmpty()) {
            return applicationRepository.findByNameContainingIgnoreCase(name, pageable)
                    .map(applicationMapper::toResponse);
        } else {
            return applicationRepository.findAll(pageable)
                    .map(applicationMapper::toResponse);
        }
    }
    
    @Override
    @Transactional
    public BusinessApplicationResponse publishApplication(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setStatus(1); // 已发布
        application.setPublishedTime(LocalDateTime.now());
        application.setUpdatedTime(LocalDateTime.now());
        
        BusinessApplication updatedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(updatedApplication);
    }
    
    @Override
    @Transactional
    public BusinessApplicationResponse offlineApplication(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setStatus(2); // 已下线
        application.setUpdatedTime(LocalDateTime.now());
        
        BusinessApplication updatedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(updatedApplication);
    }
    
    @Override
    @Transactional
    public BusinessApplicationResponse archiveApplication(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setStatus(3); // 已归档
        application.setUpdatedTime(LocalDateTime.now());
        
        BusinessApplication updatedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(updatedApplication);
    }
    
    @Override
    @Transactional
    public BusinessApplicationResponse setRecommendation(Long id, Boolean isRecommended) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setIsRecommended(isRecommended);
        application.setUpdatedTime(LocalDateTime.now());
        
        BusinessApplication updatedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(updatedApplication);
    }
    
    @Override
    @Transactional
    public BusinessApplicationResponse setPublicStatus(Long id, Boolean isPublic) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setIsPublic(isPublic);
        application.setUpdatedTime(LocalDateTime.now());
        
        BusinessApplication updatedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(updatedApplication);
    }
    
    @Override
    @Transactional
    public void incrementUsageCount(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setUsageCount(application.getUsageCount() + 1);
        application.setUpdatedTime(LocalDateTime.now());
        
        // 如果使用次数超过阈值，设置为热门应用
        if (application.getUsageCount() >= 1000) {
            application.setIsPopular(true);
        }
        
        applicationRepository.save(application);
    }
    
    @Override
    @Transactional
    public void incrementDownloadCount(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setDownloadCount(application.getDownloadCount() + 1);
        application.setUpdatedTime(LocalDateTime.now());
        applicationRepository.save(application);
    }
    
    @Override
    @Transactional
    public void incrementFavoriteCount(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setFavoriteCount(application.getFavoriteCount() + 1);
        application.setUpdatedTime(LocalDateTime.now());
        applicationRepository.save(application);
    }
    
    @Override
    @Transactional
    public void incrementReviewCount(Long id) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        application.setReviewCount(application.getReviewCount() + 1);
        application.setUpdatedTime(LocalDateTime.now());
        applicationRepository.save(application);
    }
    
    @Override
    @Transactional
    public BusinessApplicationResponse updateRating(Long id, BigDecimal rating) {
        BusinessApplication application = applicationRepository.findById(id)
                .orElseThrow(() -> new BusinessException("应用不存在，ID：" + id));
        
        // 计算新的平均评分
        BigDecimal currentRating = application.getRating();
        int reviewCount = application.getReviewCount();
        
        if (reviewCount == 0) {
            application.setRating(rating);
        } else {
            BigDecimal totalRating = currentRating.multiply(BigDecimal.valueOf(reviewCount)).add(rating);
            BigDecimal newRating = totalRating.divide(BigDecimal.valueOf(reviewCount + 1), 2, BigDecimal.ROUND_HALF_UP);
            application.setRating(newRating);
        }
        
        application.setUpdatedTime(LocalDateTime.now());
        
        BusinessApplication updatedApplication = applicationRepository.save(application);
        return applicationMapper.toResponse(updatedApplication);
    }
    
    @Override
    public Map<String, Object> getApplicationStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // 临时解决方案：手动统计应用数据
        statistics.put("totalApplications", applicationRepository.count());
        
        // 手动统计免费应用数量
        List<BusinessApplication> allApplications = applicationRepository.findAll();
        long freeCount = allApplications.stream()
                .filter(BusinessApplication::getIsFree)
                .count();
        statistics.put("freeApplications", freeCount);
        
        // 手动统计推荐应用数量
        long recommendedCount = allApplications.stream()
                .filter(BusinessApplication::getIsRecommended)
                .count();
        statistics.put("recommendedApplications", recommendedCount);
        
        return statistics;
    }
    
    @Override
    public Map<String, Long> getCategoryStatistics() {
        Map<String, Long> statistics = new HashMap<>();
        
        // 临时解决方案：手动统计分类数据
        List<BusinessApplication> allApplications = applicationRepository.findAll();
        for (BusinessApplication app : allApplications) {
            String category = app.getCategory();
            statistics.put(category, statistics.getOrDefault(category, 0L) + 1);
        }
        
        return statistics;
    }
    
    @Override
    public Map<Integer, Long> getStatusStatistics() {
        Map<Integer, Long> statistics = new HashMap<>();
        
        // 临时解决方案：手动统计状态数据
        List<BusinessApplication> allApplications = applicationRepository.findAll();
        for (BusinessApplication app : allApplications) {
            Integer status = app.getStatus();
            statistics.put(status, statistics.getOrDefault(status, 0L) + 1);
        }
        
        return statistics;
    }
}