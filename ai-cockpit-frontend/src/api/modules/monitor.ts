import { request } from '@/utils/request'
import type { PageResult, PageQuery } from '@/types/api'

// 监控数据类型定义
export interface ResourceMonitor {
  id: number
  monitorType: string
  targetType: string
  targetId: number
  targetName: string
  metricValue: number
  metricUnit: string
  status: number
  monitorTimestamp: string
  availabilityPercentage: number
  warningThreshold?: number
  criticalThreshold?: number
  monitorInterval: number
  enabled: boolean
  description: string
  createTime: string
  updateTime: string
}

// 监控历史记录
export interface MonitorHistory {
  id: number
  monitorId: number
  timestamp: string
  value: number
  status: number
  message: string
  createTime: string
}

// 监控统计信息
export interface MonitorStats {
  total: number
  normal: number
  warning: number
  critical: number
  unknown: number
  availability: number
}

// 监控配置请求参数
export interface MonitorConfigRequest {
  monitorType: string
  targetType: string
  targetId: number
  targetName: string
  metricUnit: string
  warningThreshold?: number
  criticalThreshold?: number
  monitorInterval: number
  enabled: boolean
  description: string
}

// 监控查询参数
export interface MonitorQuery extends PageQuery {
  monitorType?: string
  targetType?: string
  status?: number
  keyword?: string
  startTime?: string
  endTime?: string
}

// 历史记录查询参数
export interface HistoryQuery extends PageQuery {
  monitorId: number
  startTime?: string
  endTime?: string
  status?: number
}

// API接口定义
export class MonitorApi {
  /**
   * 获取监控列表
   */
  static getMonitorList(params: MonitorQuery) {
    return request.get<PageResult<ResourceMonitor>>('/api/v1/monitor/list', { params })
  }

  /**
   * 获取监控详情
   */
  static getMonitorDetail(id: number) {
    return request.get<ResourceMonitor>(`/api/v1/monitor/${id}`)
  }

  /**
   * 添加监控配置
   */
  static addMonitor(config: MonitorConfigRequest) {
    return request.post<ResourceMonitor>('/api/v1/monitor', config)
  }

  /**
   * 更新监控配置
   */
  static updateMonitor(id: number, config: MonitorConfigRequest) {
    return request.put<ResourceMonitor>(`/api/v1/monitor/${id}`, config)
  }

  /**
   * 删除监控配置
   */
  static deleteMonitor(id: number) {
    return request.delete(`/api/v1/monitor/${id}`)
  }

  /**
   * 启用/禁用监控
   */
  static toggleMonitor(id: number, enabled: boolean) {
    return request.patch(`/api/v1/monitor/${id}/status`, { enabled })
  }

  /**
   * 获取监控统计信息
   */
  static getMonitorStats() {
    return request.get<MonitorStats>('/api/v1/monitor/stats')
  }

  /**
   * 获取监控历史记录
   */
  static getMonitorHistory(params: HistoryQuery) {
    return request.get<PageResult<MonitorHistory>>('/api/v1/monitor/history', { params })
  }

  /**
   * 获取监控趋势数据
   */
  static getMonitorTrend(id: number, timeRange: string) {
    return request.get<Array<{ timestamp: string; value: number; status: number }>>(
      `/api/v1/monitor/${id}/trend`,
      { params: { timeRange } }
    )
  }

  /**
   * 批量更新监控状态
   */
  static batchUpdateStatus(ids: number[], enabled: boolean) {
    return request.patch('/api/v1/monitor/batch-status', { ids, enabled })
  }

  /**
   * 获取监控类型选项
   */
  static getMonitorTypes() {
    return request.get<Array<{ value: string; label: string; unit: string }>>('/api/v1/monitor/types')
  }

  /**
   * 获取目标类型选项
   */
  static getTargetTypes() {
    return request.get<Array<{ value: string; label: string }>>('/api/v1/monitor/target-types')
  }

  /**
   * 获取监控目标选项
   */
  static getTargetOptions(targetType: string) {
    return request.get<Array<{ id: number; name: string; type: string }>>(
      '/api/v1/monitor/targets',
      { params: { targetType } }
    )
  }

  /**
   * 导出监控数据
   */
  static exportMonitorData(params: MonitorQuery) {
    return request.get('/api/v1/monitor/export', {
      params,
      responseType: 'blob'
    })
  }

  /**
   * 导出历史数据
   */
  static exportHistoryData(params: HistoryQuery) {
    return request.get('/api/v1/monitor/history/export', {
      params,
      responseType: 'blob'
    })
  }

  /**
   * 获取实时监控数据（WebSocket）
   */
  static getRealtimeData(monitorIds: number[]) {
    // WebSocket连接，返回连接实例
    const ws = new WebSocket(`ws://localhost:8080/api/v1/monitor/realtime?ids=${monitorIds.join(',')}`)
    return ws
  }

  /**
   * 测试监控连接
   */
  static testMonitorConnection(config: MonitorConfigRequest) {
    return request.post('/api/v1/monitor/test-connection', config)
  }

  /**
   * 获取监控告警配置
   */
  static getAlertConfig(monitorId: number) {
    return request.get(`/api/v1/monitor/${monitorId}/alert-config`)
  }

  /**
   * 更新监控告警配置
   */
  static updateAlertConfig(monitorId: number, config: any) {
    return request.put(`/api/v1/monitor/${monitorId}/alert-config`, config)
  }
}

// 默认导出
export default MonitorApi