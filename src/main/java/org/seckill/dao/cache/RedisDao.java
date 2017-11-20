package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Redis数据访问dao
 * @author Hp
 *
 */
public class RedisDao {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	// redis数据连接池
	private JedisPool jedisPool;
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	//通过构造方法初始化redis
	public RedisDao(String ip,int port){
		jedisPool = new JedisPool(ip, port);
	}
	
	//从redis 中获取一个对象
	public Seckill getSeckill(Long seckillId){
		//redis缓存
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:"+seckillId;
				//并没有内部实现序列化的操作
				//get - byte[] ->反序列化 —> 获取Object  (Seckill)
				//采用自定义的序列化操作
				//pojo
				byte[] bytes = jedis.get(key.getBytes());
				if (bytes != null){
					//创建一个需要实例化的对象
					Seckill seckill = schema.newMessage();
					//seckill 被反序列化
					ProtostuffIOUtil.mergeFrom(bytes,seckill , schema);
					return seckill;
				}
					
				
			} finally {
				jedis.close();
			}
			
		} catch (Exception e) {
			logger.error( e.getMessage(),e);
		}
		return null;
	}
	
	/**
	 * 向redis 中放入缓存对象
	 * @param seckill
	 * @return
	 */
	public String putSeckill(Seckill seckill){
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:"+seckill.getSeckillId();
				//将对象转化为数组 实现序列化 带缓存器
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				int timeout = 60 * 60 ;//缓存时间 一小时	
				String result = jedis.setex(key.getBytes(), timeout, bytes);	
				return result;
				
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}
	
	

}