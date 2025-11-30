package com.ai.cockpit.repository;

import com.ai.cockpit.entity.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 模型数据访问接口
 */
@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {

    /**
     * 根据模型名称查询
     */
    Optional<Model> findByName(String name);

    /**
     * 根据模型类型查询
     */
    List<Model> findByType(String type);

    /**
     * 根据框架查询
     */
    List<Model> findByFramework(String framework);

    /**
     * 根据状态查询
     */
    List<Model> findByStatus(Integer status);

    /**
     * 根据创建者查询
     */
    List<Model> findByCreatedBy(Long createdBy);

    /**
     * 检查模型名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 根据名称模糊查询
     */
    List<Model> findByNameContaining(String name);

    /**
     * 根据描述模糊查询
     */
    List<Model> findByDescriptionContaining(String description);



    /**
     * 根据参数量范围查询
     */
    @Query("SELECT m FROM Model m WHERE m.parameters >= :minParams AND m.parameters <= :maxParams")
    List<Model> findByParametersBetween(@Param("minParams") Long minParams, @Param("maxParams") Long maxParams);

    /**
     * 统计模型数量
     */
    long countByType(String type);

    /**
     * 统计不同状态的模型数量
     */
    long countByStatus(Integer status);
    
    /**
     * 检查模型名称是否已存在（排除指定ID）
     */
    boolean existsByNameAndIdNot(String name, Long id);
    

}