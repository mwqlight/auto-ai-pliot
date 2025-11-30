package com.ai.cockpit.repository;

import com.ai.cockpit.entity.TrainingTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 训练任务数据访问接口
 */
@Repository
public interface TrainingTaskRepository extends JpaRepository<TrainingTask, Long> {

    /**
     * 根据任务名称查询
     */
    Optional<TrainingTask> findByName(String name);

    /**
     * 根据状态查询
     */
    List<TrainingTask> findByStatus(Integer status);

    /**
     * 根据创建者查询
     */
    List<TrainingTask> findByCreatorId(Long creatorId);

    /**
     * 根据模型ID查询
     */
    List<TrainingTask> findByModelId(Long modelId);

    /**
     * 根据数据集ID查询
     */
    List<TrainingTask> findByDatasetId(Long datasetId);

    /**
     * 检查任务名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 根据名称模糊查询
     */
    List<TrainingTask> findByNameContaining(String name);

    /**
     * 查询运行中的任务
     */
    /*@Query("SELECT t FROM TrainingTask t WHERE t.status IN (1, 2)") // 1-准备中, 2-运行中
    List<TrainingTask> findRunningTasks();*/

    /**
     * 根据进度范围查询
     */
    /*@Query("SELECT t FROM TrainingTask t WHERE t.progress >= :minProgress AND t.progress <= :maxProgress")
    List<TrainingTask> findByProgressBetween(@Param("minProgress") Integer minProgress, @Param("maxProgress") Integer maxProgress);*/

    /**
     * 统计不同状态的任务数量
     */
    long countByStatus(Integer status);

    /**
     * 统计用户的任务数量
     */
    long countByCreatorId(Long creatorId);

    /**
     * 根据任务类型查询
     */
    List<TrainingTask> findByTaskType(String taskType);

    /**
     * 查询最近的任务
     */
    List<TrainingTask> findTop10ByOrderByCreateTimeDesc();
    
    /**
     * 检查任务名称是否存在（排除指定ID）
     */
    boolean existsByNameAndIdNot(String name, Long id);
    
    /**
     * 根据名称或描述模糊查询
     */
    /*@Query("SELECT t FROM TrainingTask t WHERE t.name LIKE %:keyword% OR t.description LIKE %:keyword%")
    List<TrainingTask> findByNameContainingOrDescriptionContaining(@Param("keyword") String keyword);*/
}