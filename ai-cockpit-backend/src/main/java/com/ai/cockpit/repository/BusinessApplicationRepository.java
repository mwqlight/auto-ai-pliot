package com.ai.cockpit.repository;

import com.ai.cockpit.entity.BusinessApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 业务应用市场Repository接口
 */
@Repository
public interface BusinessApplicationRepository extends JpaRepository<BusinessApplication, Long>, JpaSpecificationExecutor<BusinessApplication> {
    
    /**
     * 根据应用代码查询应用
     */
    Optional<BusinessApplication> findByCode(String code);
    
    /**
     * 检查应用代码是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 根据应用名称模糊查询
     */
    Page<BusinessApplication> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    /**
     * 根据分类查询应用
     */
    Page<BusinessApplication> findByCategory(String category, Pageable pageable);
    
    /**
     * 根据分类和子分类查询应用
     */
    Page<BusinessApplication> findByCategoryAndSubCategory(String category, String subCategory, Pageable pageable);
    
    /**
     * 查询免费应用
     */
    Page<BusinessApplication> findByIsFreeTrue(Pageable pageable);
    
    /**
     * 查询推荐应用
     */
    Page<BusinessApplication> findByIsRecommendedTrue(Pageable pageable);
    
    /**
     * 查询热门应用
     */
    Page<BusinessApplication> findByIsPopularTrue(Pageable pageable);
    
    /**
     * 查询公开应用
     */
    Page<BusinessApplication> findByIsPublicTrue(Pageable pageable);
    
    /**
     * 根据状态查询应用
     */
    Page<BusinessApplication> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据创建者查询应用
     */
    Page<BusinessApplication> findByCreatorId(Long creatorId, Pageable pageable);
    

}