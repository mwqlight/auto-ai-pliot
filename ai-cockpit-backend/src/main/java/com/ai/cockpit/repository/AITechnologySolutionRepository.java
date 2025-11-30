package com.ai.cockpit.repository;

import com.ai.cockpit.entity.AITechnologySolution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * AI技术方案Repository接口
 */
@Repository
public interface AITechnologySolutionRepository extends JpaRepository<AITechnologySolution, Long> {
    
    /**
     * 根据方案代码查询
     */
    Optional<AITechnologySolution> findByCode(String code);
    
    /**
     * 检查方案代码是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 检查方案代码是否存在（排除指定ID）
     */
    boolean existsByCodeAndIdNot(String code, Long id);
    
    /**
     * 根据技术类型查询
     */
    Page<AITechnologySolution> findByTechnologyType(String technologyType, Pageable pageable);
    
    /**
     * 根据算法类型查询
     */
    Page<AITechnologySolution> findByAlgorithmType(String algorithmType, Pageable pageable);
    
    /**
     * 根据适用领域查询
     */
    Page<AITechnologySolution> findByApplicationDomain(String applicationDomain, Pageable pageable);
    
    /**
     * 根据状态查询
     */
    Page<AITechnologySolution> findByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据是否公开查询
     */
    Page<AITechnologySolution> findByIsPublic(Integer isPublic, Pageable pageable);
    

    
    /**
     * 查询热门方案（按使用次数排序）
     */
    Page<AITechnologySolution> findByIsPublicOrderByUsageCountDesc(Integer isPublic, Pageable pageable);
    
    /**
     * 查询评分最高的方案
     */
    Page<AITechnologySolution> findByIsPublicOrderByRatingDesc(Integer isPublic, Pageable pageable);
    
    /**
     * 查询最新发布的方案
     */
    Page<AITechnologySolution> findByIsPublicOrderByPublishedTimeDesc(Integer isPublic, Pageable pageable);
    
    /**
     * 根据创建者查询
     */
    Page<AITechnologySolution> findByCreatorId(Long creatorId, Pageable pageable);
    

}