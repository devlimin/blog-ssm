package com.limin.blog.util;

import org.omg.PortableServer.POAOperations;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.Locale;
import java.util.Set;

/**
 * Created by devlimin on 2017/7/27.
 */
@Service
public class JedisAdapter {


    @Resource
    private JedisPool jedisPool;

    public Jedis getJedis() {
        return jedisPool.getResource();
    }

    public Long incr(String key) {
        Jedis jedis = null;
        Long incr = null;
        try {
            jedis = jedisPool.getResource();
            incr = jedis.incr(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return incr;
    }

    public Long decr(String key) {
        Jedis jedis = null;
        Long decr = null;
        try {
            jedis = jedisPool.getResource();
            decr = jedis.decr(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return decr;
    }

    public String get(String key) {
        Jedis jedis = null;
        String val = null;
        try {
            jedis = jedisPool.getResource();
            val = jedis.get(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return val;
    }

    public Long del(String key) {
        Jedis jedis = null;
        Long del = null;
        try {
            jedis = jedisPool.getResource();
            del = jedis.del(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return del;
    }


    public Long sadd(String key, String ... members) {
        Jedis jedis = null;
        Long count = null;
        try {
            jedis = jedisPool.getResource();
            count = jedis.sadd(key, members);
            jedis.sadd(new byte[3], new byte[4]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return count;
    }

    public Set<String> smembers(String key) {
        Jedis jedis = null;
        Set<String> smembers = null;
        try {
            jedis = jedisPool.getResource();
            smembers = jedis.smembers(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return smembers;
    }

    public Long srem(String key, String... members) {
        Jedis jedis = null;
        Long srem = null;
        try {
            jedis = jedisPool.getResource();
            srem = jedis.srem(key, members);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return srem;
    }
    public Long scard(String key) {
        Jedis jedis = null;
        Long scard =  null;
        try {
            jedis = jedisPool.getResource();
            scard = jedis.scard(key);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return scard;
    }
}
