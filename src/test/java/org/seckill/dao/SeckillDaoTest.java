package org.seckill.dao;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 配合Spring 和Junit 整合 在junit 启动时去加载 spring 的ioc 容器
 * spring-test 和 junit
 * @author Hp
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {
	
	@Resource
	private SeckillDao seckillDao;
	
	@Test
	public void testQueryById()throws Exception{
		long id = 1L;
		Seckill seckill = seckillDao.queryById(id);
		System.out.println(seckill.getName());
		System.out.println(seckill);
		/**
		 * 1000元秒杀iPhone6s
			Seckill [seckill_id=0, name=1000元秒杀iPhone6s, number=1000, startTime=Sun Nov 01 00:00:00 CST 2015, endtime=Mon Nov 02 00:00:00 CST 2015, createTime=Wed Oct 11 19:26:19 CST 2017]

		 */
	}
	
	@Test
	public void testQueryAll()throws Exception{
		List<Seckill> list = seckillDao.queryAll(0, 100);
		for (Seckill seckill : list) {
			System.out.println(seckill);
		}
	}
	
	
	@Test
	public void testReduceNumber()throws Exception{
		long seckillId = 1L;
		Date killTime = new Date();
		int  stateCode = seckillDao.reduceNumber(seckillId, killTime);
		System.out.println(stateCode);
	}
	

	
	
	

}
