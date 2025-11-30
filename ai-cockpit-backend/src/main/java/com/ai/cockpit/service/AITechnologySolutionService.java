package com.ai.cockpit.service;

import com.ai.cockpit.dto.request.AITechnologySolutionRequest;
import com.ai.cockpit.dto.response.AITechnologySolutionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * AI技术方案服务接口
 */
public interface AITechnologySolutionService {
    
    /**
     * 创建AI技术方案
     */
    AITechnologySolutionResponse createSolution(AITechnologySolutionRequest request, Long creatorId);
    
    /**
     * 更新AI技术方案
     */
    AITechnologySolutionResponse updateSolution(Long id, AITechnologySolutionRequest request);
    
    /**
     * 删除AI技术方案
     */
    void deleteSolution(Long id);
    
    /**
     * 根据ID查询AI技术方案
     */
    AITechnologySolutionResponse getSolutionById(Long id);
    
    /**
     * 根据代码查询AI技术方案
     */
    AITechnologySolutionResponse getSolutionByCode(String code);
    
    /**
     * 分页查询所有AI技术方案
     */
    Page<AITechnologySolutionResponse> getAllSolutions(Pageable pageable);
    
    /**
     * 根据技术类型查询AI技术方案
     */
    Page<AITechnologySolutionResponse> getSolutionsByTechnologyType(String technologyType, Pageable pageable);
    
    /**
     * 根据算法类型查询AI技术方案
     */
    Page<AITechnologySolutionResponse> getSolutionsByAlgorithmType(String algorithmType, Pageable pageable);
    
    /**
     * 根据适用领域查询AI技术方案
     */
    Page<AITechnologySolutionResponse> getSolutionsByApplicationDomain(String applicationDomain, Pageable pageable);
    
    /**
     * 根据状态查询AI技术方案
     */
    Page<AITechnologySolutionResponse> getSolutionsByStatus(Integer status, Pageable pageable);
    
    /**
     * 根据是否公开查询AI技术方案
     */
    Page<AITechnologySolutionResponse> getSolutionsByIsPublic(Integer isPublic, Pageable pageable);
    
    /**
     * 根据名称或描述模糊查询AI技术方案
     */
    Page<AITechnologySolutionResponse> searchSolutions(String keyword, Integer status, Pageable pageable);
    
    /**
     * 根据多个条件组合查询AI技术方案
     */
    Page<AITechnologySolutionResponse> getSolutionsByMultipleCriteria(
            String technologyType, String algorithmType, String applicationDomain, 
            Integer status, Integer isPublic, Pageable pageable);
    
    /**
     * 根据标签查询AI技术方案
     */
    Page<AITechnologySolutionResponse> getSolutionsByTag(String tag, Pageable pageable);
    
    /**
     * 查询热门AI技术方案（按使用次数排序）
     */
    Page<AITechnologySolutionResponse> getPopularSolutions(Pageable pageable);
    
    /**
     * 查询评分最高的AI技术方案
     */
    Page<AITechnologySolutionResponse> getTopRatedSolutions(Pageable pageable);
    
    /**
     * 查询最新发布的AI技术方案
     */
    Page<AITechnologySolutionResponse> getLatestSolutions(Pageable pageable);
    
    /**
     * 根据创建者查询AI技术方案
     */
    Page<AITechnologySolutionResponse> getSolutionsByCreator(Long creatorId, Pageable pageable);
    
    /**
     * 发布AI技术方案
     */
    AITechnologySolutionResponse publishSolution(Long id);
    
    /**
     * 下线AI技术方案
     */
    AITechnologySolutionResponse unpublishSolution(Long id);
    
    /**
     * 归档AI技术方案
     */
    AITechnologySolutionResponse archiveSolution(Long id);
    
    /**
     * 增加使用次数
     */
    void incrementUsageCount(Long id);
    
    /**
     * 更新评分
     */
    AITechnologySolutionResponse updateRating(Long id, BigDecimal rating);
    
    /**
     * 统计各技术类型的方案数量
     */
    List<Object[]> countSolutionsByTechnologyType();
    
    /**
     * 统计各状态的方案数量
     */
    List<Object[]> countSolutionsByStatus();
    
    /**
     * 检查方案代码是否存在
     */
    boolean existsByCode(String code);
    
    /**
     * 检查方案代码是否存在（排除指定ID）
     */
    boolean existsByCodeAndIdNot(String code, Long id);
}