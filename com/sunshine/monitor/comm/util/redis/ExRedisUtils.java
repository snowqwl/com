package com.sunshine.monitor.comm.util.redis;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sunshine.monitor.comm.maintain.util.FileSystemResource;
import com.sunshine.monitor.comm.util.StringUtils;

public class ExRedisUtils
{
  protected static final Logger log = Logger.getLogger(ExRedisUtils.class);
  private static String redisServer;
  private static String redisPort;
  private static String redisPassword;
  private static JedisPoolConfig config = new JedisPoolConfig();
  private static int port = 0;
  static
  {
    if (redisServer == null) {
/*      if (GlobalConfig.isTestEnv()) {
        redisServer = GlobalConfig.getConfigValue("test.redis.server");
        redisPort = GlobalConfig.getConfigValue("test.redis.port");
        redisPassword = GlobalConfig.getConfigValue("test.redis.password");
      } else {
        redisServer = GlobalConfig.getConfigValue("redis.server");
        redisPort = GlobalConfig.getConfigValue("redis.port");
        redisPassword = GlobalConfig.getConfigValue("redis.password");
      }*/
    	Properties property = FileSystemResource.getProperty("common.properties");
    	redisServer = property.getProperty("redis.server", "10.142.54.37");
        redisPort = property.getProperty("redis.port", "6379");
        redisPassword = property.getProperty("redis.password","ygnet@20160101");
    }

/*    if (redisServer == null) {
      redisServer = "localhost";
    }*/

    config.setMaxTotal(10);
    config.setMaxIdle(2);
    config.setMaxWaitMillis(10000L);
    config.setTestOnBorrow(true);
    port = StringUtils.parseInt(redisPort, 6739);
  }
  private static JedisPool jedisPool = new JedisPool(config, redisServer, port, 10000, redisPassword);

  public static Jedis getClicent()
  {
    Jedis jedisClient = jedisPool.getResource();
    return jedisClient;
  }

  public static void returnResource(Jedis jedis)
  {
    jedisPool.returnResource(jedis);
  }

  public static void put(String key, String value, int seconds) {
    Jedis jedis = getClicent();
    jedis.set(key, value);
    jedis.expire(key, seconds);
    returnResource(jedis);
  }
  public static void put(String key, String value) {
    Jedis jedis = getClicent();
    jedis.set(key, value);
    returnResource(jedis);
  }

  public static String hget(String key, String field) {
    Jedis jedis = getClicent();
    String value = jedis.hget(key, field);
    returnResource(jedis);
    return value;
  }

  public static Long hlen(String key) {
    Jedis jedis = getClicent();
    Long value = jedis.hlen(key);
    returnResource(jedis);
    return value;
  }

  public static Set<String> hkeys(String key) {
    Jedis jedis = getClicent();
    Set value = jedis.hkeys(key);
    returnResource(jedis);
    return value;
  }

  public static Map hgetAll(String key) {
    Jedis jedis = getClicent();
    Map value = jedis.hgetAll(key);
    returnResource(jedis);
    return value;
  }

  public static long hset(String key, String field, String value) {
    Jedis jedis = getClicent();
    long result = jedis.hset(key, field, value).longValue();
    returnResource(jedis);
    return result;
  }

  public static String hmset(String key, Map hash) {
    Jedis jedis = getClicent();
    String value = jedis.hmset(key, hash);
    returnResource(jedis);
    return value;
  }

  public static String testCmd(String key)
  {
    try {
      long t1 = System.currentTimeMillis();
      Jedis jedis = getClicent();
      log.info("time1=" + (System.currentTimeMillis() - t1));
      t1 = System.currentTimeMillis();
      String value = jedis.get(key);

      log.info("time2=" + (System.currentTimeMillis() - t1));
      t1 = System.currentTimeMillis();

      for (int i = 0; i < 10; i++) {
        put(key, 100);
      }
      log.info("time21=" + (System.currentTimeMillis() - t1));
      t1 = System.currentTimeMillis();

      for (int i = 0; i < 10; i++) {
        put(key, 100);
        get(key);
      }
      log.info("time22=" + (System.currentTimeMillis() - t1));
      t1 = System.currentTimeMillis();

      returnResource(jedis);
      log.info("time3=" + (System.currentTimeMillis() - t1));

      return value;
    }
    catch (Exception ex) {
      log.error(ex);
    }

    return null;
  }

  public static String get(String key)
  {
    try
    {
      Jedis jedis = getClicent();
      String value = jedis.get(key);
      returnResource(jedis);
      return value;
    }
    catch (Exception ex) {
      log.error(ex);
    }

    return null;
  }

  public static Long expire(String key, int seconds)
  {
    Jedis jedis = getClicent();
    Long value = jedis.expire(key, seconds);
    returnResource(jedis);
    return value;
  }

  public static Long expireAt(String key, int unixTime)
  {
    Jedis jedis = getClicent();
    Long value = jedis.expireAt(key, unixTime);
    returnResource(jedis);
    return value;
  }

  public static Long getDbSize() {
    Jedis jedis = getClicent();
    Long dbSize = jedis.dbSize();
    returnResource(jedis);

    return dbSize;
  }

  public static Set getKeys(String pattern) {
    Jedis jedis = getClicent();
    Set keys = jedis.keys(pattern);
    returnResource(jedis);
    return keys;
  }

  public static void put(String key, int value, int seconds) {
    put(key, String.valueOf(value), seconds);
  }

  public static void put(String key, int value) {
    put(key, String.valueOf(value));
  }

  public static int getInt(String key)
  {
    try {
      String value = get(key);
      return Integer.parseInt(value); } catch (Exception ex) {
    }
    return 0;
  }

  public static void putMap(String key, Map map, int seconds)
  {
    put(key, JSON.toJSONString(map), seconds);
  }

  public static void putMap(String key, Map map) {
    put(key, JSON.toJSONString(map));
  }

  public static Map getMap(String key)
  {
    String jsonStr = get(key);
    if (jsonStr != null) {
      JSONObject tmpJson = JSON.parseObject(jsonStr);
      return (Map)JSON.toJavaObject(tmpJson, Map.class);
    }
    return null;
  }

  public static void put(String key, long value, int seconds) {
    put(key, String.valueOf(value), seconds);
  }

  public static void put(String key, long value) {
    put(key, String.valueOf(value));
  }

  public static long getLong(String key)
  {
    try {
      String value = get(key);
      return Long.parseLong(value); } catch (Exception ex) {
    }
    return 0L;
  }

  public static void incr(String key)
  {
    Jedis jedis = getClicent();
    jedis.incr(key);
    returnResource(jedis);
  }

  public static long lpush(String key, String message) {
    Jedis jedis = getClicent();
    long size = jedis.lpush(key, new String[] { message }).longValue();
    returnResource(jedis);
    return size;
  }

  public static String lpop(String key) {
    Jedis jedis = getClicent();
    String value = jedis.rpop(key);
    returnResource(jedis);
    return value;
  }

  public static long rpush(String key, String message) {
    Jedis jedis = getClicent();
    long size = jedis.lpush(key, new String[] { message }).longValue();
    returnResource(jedis);
    return size;
  }

  public static String rpop(String key) {
    Jedis jedis = getClicent();
    String value = jedis.rpop(key);
    returnResource(jedis);
    return value;
  }

  public static String ltrim(String key, long start, long end)
  {
    Jedis jedis = getClicent();
    String result = jedis.ltrim(key, start, end);
    returnResource(jedis);
    return result;
  }

  public static List lrange(String key, long start, long end) {
    Jedis jedis = getClicent();
    List resultList = jedis.lrange(key, start, end);
    returnResource(jedis);
    return resultList;
  }

  public static List<String> brpop(String key, int timeout) {
    Jedis jedis = getClicent();
    List resultList = jedis.brpop(timeout, key);
    returnResource(jedis);
    return resultList;
  }

  public static void publishMessage(String channel, String message)
  {
    Jedis jedis = getClicent();
    jedis.publish(channel, message);
    returnResource(jedis);
  }

  public static void closeMessageChannel(String channel)
  {
    Jedis jedis = getClicent();
    jedis.publish(channel, "quit");
    jedis.del(channel);
    returnResource(jedis);
  }

  public static boolean isRunning()
  {
    boolean result = false;
    try {
      Jedis jedis = getClicent();
      String value = String.valueOf(System.currentTimeMillis());
      jedis.set("time", value);
      if (value.equalsIgnoreCase(jedis.get("time"))) {
        result = true;
      }
      returnResource(jedis);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      result = false;
    }

    return result;
  }

  public static long llen(String key) {
    Jedis jedis = getClicent();
    long length = jedis.llen(key).longValue();
    returnResource(jedis);
    return length;
  }

  public static long del(String key) {
    Jedis jedis = getClicent();
    long length = jedis.del(key).longValue();
    returnResource(jedis);
    return length;
  }

  public static long getNumActive() {
    return jedisPool.getNumActive();
  }

  public static double zincrby(String key, String member, double score) {
    Jedis jedis = getClicent();
    double value = jedis.zincrby(key, score, member).doubleValue();
    returnResource(jedis);
    return value;
  }

  public static double zadd(String key, String member, double score) {
    Jedis jedis = getClicent();
    double value = jedis.zadd(key, score, member).longValue();
    returnResource(jedis);
    return value;
  }

  public static double zscore(String key, String member) {
    Jedis jedis = getClicent();
    double value = jedis.zscore(key, member).doubleValue();
    returnResource(jedis);
    return value;
  }

  public static Set zrange(String key, long start, long end) {
    Jedis jedis = getClicent();
    Set value = jedis.zrange(key, start, end);
    returnResource(jedis);
    return value;
  }

  public static Set zrangeWithScores(String key, long start, long end) {
    Jedis jedis = getClicent();
    Set value = jedis.zrangeWithScores(key, start, end);
    returnResource(jedis);
    return value;
  }

  public static Set zrangeByScore(String key, double min, double max) {
    Jedis jedis = getClicent();
    Set value = jedis.zrangeByScore(key, min, max);
    returnResource(jedis);
    return value;
  }

  public static long zcount(String key, double min, double max)
  {
    Jedis jedis = getClicent();
    long value = jedis.zcount(key, min, max).longValue();
    returnResource(jedis);
    return value;
  }

  /*public static void main(String[] args)
  {
    long t1 = System.currentTimeMillis();

    Jedis jedis = getClicent();
    for (int i = 0; i < 100000; i++)
    {
      jedis.zincrby("CarCount3", 1.0D, "test");
    }

    returnResource(jedis);

    System.out.println("time=" + (System.currentTimeMillis() - t1));
  }*/
}