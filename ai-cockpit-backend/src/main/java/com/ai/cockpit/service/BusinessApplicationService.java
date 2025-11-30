package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.BusinessApplicationRequest;
import com.ai.cockpit.dto.response.BusinessApplicationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 业务应用市场Service接口
 */
public interface BusinessApplicationService {
    
    /**
     * 创建应用
     */
    BusinessApplicationResponse createApplication(BusinessApplicationRequest request);
    
    /**
     * 更新应用
     */
    BusinessApplicationResponse updateApplication(Long id, BusinessApplicationRequest request);
    
    /**
     * 删除应用
     */
    void deleteApplication(Long id);
    
    /**
     * 根据ID获取应用
     */
    BusinessApplicationResponse getApplicationById(Long id);
    
    /**
     * 根据应用代码获取应用
     */
    BusinessApplicationResponse getApplicationByCode(String code);
    
    /**
     * 分页查询所有应用
     */
    Page<BusinessApplicationResponse> getAllApplications(Pageable pageable);
    
    /**
     * 根据名称搜索应用
     */
    Page<BusinessApplicationResponse> searchApplicationsByName(String name, Pageable pageable);
    
    /**
     * 根据分类查询应用
     */
    Page<BusinessApplicationResponse> getApplicationsByCategory(String category, Pageable pageable);
    
    /**
     * 根据分类和子分类查询应用
     */
    Page<BusinessApplicationResponse> getApplicationsByCategoryAndSubCategory(String category, String subCategory, Pageable pageable);
    
    /**
     * 查询免费应用
     */
    Page<BusinessApplicationResponse> getFreeApplications(Pageable pageable);
    
    /**
     * 查询推荐应用
     */
    Page<BusinessApplicationResponse> getRecommendedApplications(Pageable pageable);
    
    /**
     * 查询热门应用
     */
    Page<BusinessApplicationResponse> getPopularApplications(Pageable pageable);
    
    /**
     * 查询公开应用
     */
    Page<BusinessApplicationResponse> getPublicApplications(Pageable pageable);
    
    /**
     * 根据状态查询应用
     */
    Page<BusinessApplicationResponse> getApplicationsByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据创建者查询应用
     */
    Page<BusinessApplicationResponse> getApplicationsByCreator(Long creatorId, Pageable pageable);
    
    /**
     * 根据标签查询应用
     */
    Page<BusinessApplicationResponse> getApplicationsByTag(String tag, Pageable pageable);
    
    /**
     * 多条件查询应用
     */
    Page<BusinessApplicationResponse> searchApplications(String name, String category, Boolean isFree, Integer status, Pageable pageable);
    
    /**
     * 发布应用
     */
    BusinessApplicationResponse publishApplication(Long id);
    
    /**
     * 下线应用
     */
    BusinessApplicationResponse offlineApplication(Long id);
    
    /**
     * 归档应用
     */
    BusinessApplicationResponse archiveApplication(Long id);
    
    /**
     * 设置应用推荐状态
     */
    BusinessApplicationResponse setRecommendation(Long id, Boolean isRecommended);
    
    /**
     * 设置应用公开状态
     */
    BusinessApplicationResponse setPublicStatus(Long id, Boolean isPublic);
    
    /**
     * 增加应用使用次数
     */
    void incrementUsageCount(Long id);
    
    /**
     * 增加应用下载次数
     */
    void incrementDownloadCount(Long id);
    
    /**
     * 增加应用收藏次数
     */
    void incrementFavoriteCount(Long id);
    
    /**
     * 增加应用评论次数
     */
    void incrementReviewCount(Long id);
    
    /**
     * 更新应用评分
     */
    BusinessApplicationResponse updateRating(Long id, BigDecimal rating);
    
    /**
     * 获取应用统计信息
     */
    Map<String, Object> getApplicationStatistics();
    
    /**
     * 获取分类统计
     */
    Map<String, Long> getCategoryStatistics();
    
    /**
     * 获取状态统计
     */
    Map<Integer, Long> getStatusStatistics();
}