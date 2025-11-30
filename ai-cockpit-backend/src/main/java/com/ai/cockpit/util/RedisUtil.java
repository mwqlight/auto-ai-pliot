package com.ai.cockpit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
@Slf4j
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // ============================== 通用操作 ==============================

    /**
     * 指定缓存失效时间
     * @param key 键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存过期时间失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("检查key是否存在失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 删除缓存
     * @param key 可以传一个值 或多个
     */
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) List.of(key));
            }
        }
    }

    /**
     * 删除缓存（别名方法）
     * @param key 要删除的键
     */
    public void delete(String key) {
        if (key != null) {
            redisTemplate.delete(key);
        }
    }

    // ============================== String操作 ==============================

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("设置缓存失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 递增
     * @param key 键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     * @param key 键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    // ============================== Hash操作 ==============================

    /**
     * HashGet
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("设置hash缓存失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key 键
     * @param item 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("设置hash缓存失败, key: {}, item: {}", key, item, e);
            return false;
        }
    }

    /**
     * 删除hash表中的值
     * @param key 键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key 键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    // ============================== Set操作 ==============================

    /**
     * 根据key获取Set中的所有值
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("获取set缓存失败, key: {}", key, e);
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     * @param key 键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
        } catch (Exception e) {
            log.error("检查set成员失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     * @param key 键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("设置set缓存失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("获取set大小失败, key: {}", key, e);
            return 0;
        }
    }

    /**
     * 移除值为value的
     * @param key 键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public long setRemove(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().remove(key, values);
        } catch (Exception e) {
            log.error("移除set成员失败, key: {}", key, e);
            return 0;
        }
    }

    // ============================== ZSet操作 ==============================

    /**
     * 向ZSet中添加元素
     * @param key 键
     * @param value 值
     * @param score 分数
     * @return
     */
    public boolean zAdd(String key, Object value, double score) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, value, score));
        } catch (Exception e) {
            log.error("添加zset成员失败, key: {}", key, e);
            return false;
        }
    }

    /**
     * 获取ZSet中指定范围的元素
     * @param key 键
     * @param start 开始位置
     * @param end 结束位置
     * @return
     */
    public Set<Object> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        } catch (Exception e) {
            log.error("获取zset范围失败, key: {}", key, e);
            return null;
        }
    }

    // ============================== 业务相关操作 ==============================

    /**
     * 缓存用户token
     * @param userId 用户ID
     * @param token token
     * @param expireTime 过期时间(秒)
     */
    public void cacheUserToken(Long userId, String token, long expireTime) {
        String key = "user:token:" + userId;
        set(key, token, expireTime);
    }

    /**
     * 获取用户token
     * @param userId 用户ID
     * @return token
     */
    public String getUserToken(Long userId) {
        String key = "user:token:" + userId;
        Object token = get(key);
        return token != null ? token.toString() : null;
    }

    /**
     * 删除用户token
     * @param userId 用户ID
     */
    public void deleteUserToken(Long userId) {
        String key = "user:token:" + userId;
        del(key);
    }

    /**
     * 缓存验证码
     * @param key 验证码key
     * @param code 验证码
     * @param expireTime 过期时间(秒)
     */
    public void cacheCaptcha(String key, String code, long expireTime) {
        String cacheKey = "captcha:" + key;
        set(cacheKey, code, expireTime);
    }

    /**
     * 获取验证码
     * @param key 验证码key
     * @return 验证码
     */
    public String getCaptcha(String key) {
        String cacheKey = "captcha:" + key;
        Object code = get(cacheKey);
        return code != null ? code.toString() : null;
    }

    /**
     * 删除验证码
     * @param key 验证码key
     */
    public void deleteCaptcha(String key) {
        String cacheKey = "captcha:" + key;
        del(cacheKey);
    }

    /**
     * 缓存模型训练进度
     * @param taskId 任务ID
     * @param progress 进度
     */
    public void cacheTrainingProgress(Long taskId, double progress) {
        String key = "training:progress:" + taskId;
        set(key, progress, 3600); // 缓存1小时
    }

    /**
     * 获取模型训练进度
     * @param taskId 任务ID
     * @return 进度
     */
    public Double getTrainingProgress(Long taskId) {
        String key = "training:progress:" + taskId;
        Object progress = get(key);
        return progress != null ? Double.parseDouble(progress.toString()) : 0.0;
    }

    /**
     * 缓存API限流计数
     * @param key 限流key
     * @param count 计数
     * @param expireTime 过期时间
     */
    public void cacheRateLimit(String key, long count, long expireTime) {
        String cacheKey = "rate:limit:" + key;
        set(cacheKey, count, expireTime);
    }

    /**
     * 获取API限流计数
     * @param key 限流key
     * @return 计数
     */
    public Long getRateLimit(String key) {
        String cacheKey = "rate:limit:" + key;
        Object count = get(cacheKey);
        return count != null ? Long.parseLong(count.toString()) : 0L;
    }

    /**
     * 清空所有缓存
     */
    public void flushAll() {
        Set<String> keys = redisTemplate.keys("*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}