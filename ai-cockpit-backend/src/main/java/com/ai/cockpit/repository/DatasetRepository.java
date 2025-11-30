package com.ai.cockpit.repository;

import com.ai.cockpit.entity.Dataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 数据集数据访问接口
 */
@Repository
public interface DatasetRepository extends JpaRepository<Dataset, Long> {

    /**
     * 根据数据集名称查询
     */
    Optional<Dataset> findByName(String name);

    /**
     * 根据数据集类型查询
     */
    List<Dataset> findByType(String type);

    /**
     * 根据格式查询
     */
    List<Dataset> findByFormat(String format);

    /**
     * 根据状态查询
     */
    List<Dataset> findByStatus(Integer status);

    /**
     * 根据创建者查询
     */
    List<Dataset> findByCreatorId(Long creatorId);

    /**
     * 检查数据集名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 根据名称模糊查询
     */
    List<Dataset> findByNameContaining(String name);

    /**
     * 根据描述模糊查询
     */
    List<Dataset> findByDescriptionContaining(String description);

    /**
     * 查询公开数据集
     */
    List<Dataset> findByIsPublicTrue();

    /**
     * 根据数据量范围查询
     */
    @Query("SELECT d FROM Dataset d WHERE d.dataCount >= :minCount AND d.dataCount <= :maxCount")
    List<Dataset> findByDataCountBetween(@Param("minCount") Long minCount, @Param("maxCount") Long maxCount);

    /**
     * 统计不同类型的数据集数量
     */
    long countByType(String type);

    /**
     * 统计不同状态的数据集数量
     */
    long countByStatus(Integer status);

    /**
     * 检查数据集名称是否存在（排除指定ID）
     */
    boolean existsByNameAndIdNot(String name, Long id);
    

    
    /**
     * 根据标签模糊查询
     */
    @Query("SELECT d FROM Dataset d WHERE d.tags LIKE CONCAT('%', :tag, '%')")
    List<Dataset> findByTagsContaining(@Param("tag") String tag);
    
    /**
     * 根据数据来源查询
     */
    List<Dataset> findBySource(String source);
}