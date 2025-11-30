package com.ai.cockpit.repository;

import com.ai.cockpit.entity.ResourceMonitor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 资源监控数据访问接口
 */
@Repository
public interface ResourceMonitorRepository extends JpaRepository<ResourceMonitor, Long>, JpaSpecificationExecutor<ResourceMonitor> {
    
    /**
     * 根据监控类型和目标类型查找
     */
    List<ResourceMonitor> findByMonitorTypeAndTargetType(String monitorType, String targetType);
    
    /**
     * 根据目标类型和目标ID查找
     */
    List<ResourceMonitor> findByTargetTypeAndTargetId(String targetType, Long targetId);
    
    /**
     * 根据监控类型、目标类型和目标ID查找
     */
    Optional<ResourceMonitor> findByMonitorTypeAndTargetTypeAndTargetId(String monitorType, String targetType, Long targetId);
    
    /**
     * 根据状态查找
     */
    List<ResourceMonitor> findByStatus(Integer status);
    
    /**
     * 根据是否启用查找
     */
    List<ResourceMonitor> findByIsEnabled(Boolean isEnabled);
    
    /**
     * 根据是否启用统计数量
     */
    Long countByIsEnabled(Boolean isEnabled);
    
    /**
     * 根据监控时间范围查找
     */
    List<ResourceMonitor> findByMonitorTimestampBetween(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 根据状态统计数量
     */
    Long countByStatus(Integer status);
    
    /**
     * 根据监控类型、目标类型和状态查找
     */
    Page<ResourceMonitor> findByMonitorTypeAndTargetTypeAndStatus(String monitorType, String targetType, Integer status, Pageable pageable);
    
    /**
     * 根据监控类型和状态统计数量
     */
    Long countByMonitorTypeAndStatus(String monitorType, Integer status);
    
    /**
     * 根据目标类型和状态统计数量
     */
    Long countByTargetTypeAndStatus(String targetType, Integer status);
    
    /**
     * 查找需要检查的监控项（启用且最后检查时间早于指定时间）
     */
    @Query("SELECT rm FROM ResourceMonitor rm WHERE rm.isEnabled = true AND rm.lastCheckTime < :checkTime")
    List<ResourceMonitor> findMonitorsNeedCheck(@Param("checkTime") LocalDateTime checkTime);
    
    /**
     * 查找处于警告或严重状态的监控项
     */
    @Query("SELECT rm FROM ResourceMonitor rm WHERE rm.status IN (1, 2)")
    List<ResourceMonitor> findWarningOrCriticalMonitors();
    
    /**
     * 根据监控类型统计各状态数量
     */
    @Query("SELECT rm.monitorType, rm.status, COUNT(rm) FROM ResourceMonitor rm GROUP BY rm.monitorType, rm.status")
    List<Object[]> countByMonitorTypeAndStatusGroup();
    
    /**
     * 根据目标类型统计各状态数量
     */
    @Query("SELECT rm.targetType, rm.status, COUNT(rm) FROM ResourceMonitor rm GROUP BY rm.targetType, rm.status")
    List<Object[]> countByTargetTypeAndStatusGroup();
    
    /**
     * 查找指定时间范围内的监控数据
     */
    /*@Query("SELECT rm FROM ResourceMonitor rm WHERE rm.monitorTimestamp BETWEEN :startTime AND :endTime AND rm.monitorType = :monitorType AND rm.targetType = :targetType")
    List<ResourceMonitor> findMonitorDataByTimeRange(@Param("startTime") LocalDateTime startTime, 
                                                    @Param("endTime") LocalDateTime endTime,
                                                    @Param("monitorType") String monitorType,
                                                    @Param("targetType") String targetType);*/
    
    /**
     * 查找最新的监控数据
     */
    /*@Query("SELECT rm FROM ResourceMonitor rm WHERE rm.targetType = :targetType AND rm.targetId = :targetId AND rm.monitorType = :monitorType ORDER BY rm.monitorTimestamp DESC")
    Optional<ResourceMonitor> findLatestMonitorData(@Param("targetType") String targetType, 
                                                   @Param("targetId") Long targetId,
                                                   @Param("monitorType") String monitorType);*/
    
    /**
     * 查找指定时间点之前的监控数据
     */
    List<ResourceMonitor> findByMonitorTimestampBefore(LocalDateTime beforeTime);
}