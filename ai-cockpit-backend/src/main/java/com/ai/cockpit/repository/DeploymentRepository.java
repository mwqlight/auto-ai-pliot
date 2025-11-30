package com.ai.cockpit.repository;

import com.ai.cockpit.entity.Deployment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 部署数据访问接口
 */
@Repository
public interface DeploymentRepository extends JpaRepository<Deployment, Long> {

    /**
     * 根据部署名称查询
     */
    Optional<Deployment> findByName(String name);

    /**
     * 根据状态查询
     */
    List<Deployment> findByStatus(Integer status);

    /**
     * 根据模型ID查询
     */
    List<Deployment> findByModelId(Long modelId);

    /**
     * 根据创建者查询
     */
    List<Deployment> findByCreatorId(Long creatorId);

    /**
     * 检查部署名称是否存在
     */
    boolean existsByName(String name);

    /**
     * 根据名称模糊查询
     */
    List<Deployment> findByNameContaining(String name);

    /**
     * 查询运行中的部署
     */
    /*@Query("SELECT d FROM Deployment d WHERE d.status = 1") // 1-运行中
    List<Deployment> findRunningDeployments();*/

    /**
     * 根据环境查询
     */
    List<Deployment> findByEnvironment(String environment);

    /**
     * 统计不同状态的部署数量
     */
    long countByStatus(Integer status);

    /**
     * 统计用户的部署数量
     */
    long countByCreatorId(Long creatorId);

    /**
     * 根据部署类型查询
     */
    List<Deployment> findByDeploymentType(String deploymentType);

    /**
     * 查询最近的部署
     */
    List<Deployment> findTop10ByOrderByCreateTimeDesc();

    /**
     * 根据API端点查询
     */
    Optional<Deployment> findByApiEndpoint(String apiEndpoint);
}