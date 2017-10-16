package org.seckill.service;

import static org.junit.Assert.*;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})

public class SeckillServiceTest {

	@Resource
	private SeckillService seckillservice;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Test
	public void testGetSeckillList() {
	List<Seckill> list = seckillservice.getSeckillList();
	logger.info("list={}", list);
	
		
	}

	@Test
	public void testGetById() {
		long id = 1L;
		Seckill seckill = seckillservice.getById(id);
		logger.info("seckill={}",seckill );
	}

	@Test
	public void testSeckillLogic() throws Exception {
		long seckillId = 1L;
		Exposer exposer = seckillservice.exportSeckillUrl(seckillId);
		if(exposer.isExposed()){
			logger.info("exposer={}",exposer);
			String md5 = exposer.getMd5();
			long userPhone = 18625776286L;
			try {
				SeckillExecution seckill = seckillservice.exectueSeckill(seckillId, userPhone, md5);
				logger.info("seckill={}",seckill);
			} catch (RepeatKillException e) {
				logger.error(e.getMessage());
			}catch (SeckillCloseException e) {
			logger.error(e.getMessage());
			}
		}else{
			//秒杀未开启
			logger.error("expeoser={}",exposer);
		}
	}

}
