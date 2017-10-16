package org.seckill.service;
import java.util.List;
/**
 * 业务接口：站在“使用者”的角度设计接口
 * 三个方面： 方法定义力度， 参数， 返回值类型（return 类型/）
 * @author Hp
 *
 */

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
public interface SeckillService {
	
	/**
	 *查询所有的秒杀记录 
	 * @return
	 */
	List<Seckill> getSeckillList();
	
	/**
	 * 获得单个秒杀记录
	 * @param seckillId
	 * @return
	 */
	Seckill getById(long seckillId);
	
	/**
	 * 秒杀开启时输出秒杀接口地址
	 * 否则输出秒杀时间和系统时间
	 * @param seckillId
	 */
	Exposer exportSeckillUrl (long seckillId);
	
	/**
	 * 执行秒杀操作
	 * @param seckillId
	 * @param userPhone
	 * @param md5
	 * @return
	 * @throws SeckillException
	 * @throws RepeatKillException
	 * @throws SeckillCloseException
	 */
	SeckillExecution exectueSeckill(long seckillId ,long userPhone, String md5) 
			throws SeckillException ,RepeatKillException, SeckillCloseException;
}
