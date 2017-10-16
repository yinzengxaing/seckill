package org.seckill.dao;

import static org.junit.Assert.*;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
//告诉junit spring 的配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

	@Resource
	private SuccessKilledDao successKilledDaoTest;
	
	@Test
	public void testInsertSuccessKilled () throws Exception {
		long seckillId = 3L;
		long userPhone = 18625776286L;
		int returnCode = successKilledDaoTest.insertSuccessKilled(seckillId, userPhone);
		System.out.println(returnCode);
	}
	
	@Test
	public void testQueryByIdWithSeckill () throws Exception {
		long seckillId = 3L;
		long userPhone = 18625776286L;
		SuccessKilled successKilled = successKilledDaoTest.queryByIdWithSeckill(seckillId, userPhone);
		System.out.println(successKilled);
	}

}
