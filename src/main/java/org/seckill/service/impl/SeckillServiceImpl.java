package org.seckill.service.impl;

import java.util.Date;
import java.util.List;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

@Service
public class SeckillServiceImpl implements SeckillService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private SeckillDao seckillDao;
	@Autowired
	private SuccessKilledDao successKilledDao;
	
	//md5盐值字符串 用于混淆
	private final String slat = "kljsalkjhelk21jlk4(*(*^&w32LKdakjn1$#&^(*)KM";
	
	@Override
	public List<Seckill> getSeckillList() {
		List<Seckill> queryAll = seckillDao.queryAll(0, 4);
		System.out.println(queryAll);
		return seckillDao.queryAll(0, 4);
	}

	@Override
	public Seckill getById(long seckillId) {
		return seckillDao.queryById(seckillId);
	}

	@Override
	public Exposer exportSeckillUrl(long seckillId) {
		Seckill seckill = seckillDao.queryById(seckillId);
		if(seckill == null){
			return new Exposer(false, seckillId); 
		}
		
		Date startTime = seckill.getStartTime();
		Date endtime = seckill.getEndTime();
		//系统当前时间
		Date now = new Date();
		if (startTime.getTime() > now.getTime() || now.getTime() > endtime.getTime()){
			return new Exposer(false, seckillId, now.getTime(), startTime.getTime(), endtime.getTime());
		}
		
		String md5 = getMD5(seckillId) ;
		return new Exposer(true, md5, seckillId);
	}

	@Override
	@Transactional
	/**
	 * 使用注解控制事物方法的优点
	 * 1：开发团队达成一致的约定，明确标注事物方法的编程风格
	 * 2：方法的时间尽可能的短
	 * 3.不是所有的方法都需要事物管理
	 */
	public SeckillExecution exectueSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
		
		if( md5 == null || !md5.equals(getMD5(seckillId))){
			throw new SeckillCloseException("seckill data rewite");
		}
		try {
			Date nowTime = new Date();
			//执行秒杀业务逻辑 减库存 ＋ 记录购买行为
			//减库存
			int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
			if (updateCount <= 0){
				//没有更新到记录 秒杀结束
				throw new SeckillCloseException("seckill is closed ");
			} else {
				//秒杀成功
				int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
				
				if (insertCount < 0){
					//重复秒杀
					throw new RepeatKillException("seckill repeated");
				}else{
					//秒杀成功
					SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
					return new SeckillExecution(seckillId,SeckillStateEnum.SUCCESS, successKilled);
				}
			}
		} catch (SeckillCloseException e1) {
			throw e1; //秒杀关闭异常
		} catch (RepeatKillException e2) {
			throw e2; //秒杀重复异常
		}catch (Exception e) {
			logger.error(e.getMessage(), e);
			//将所有编译期异常 转化为 运行期异常
			throw new SeckillException("seckill inner error"+e.getMessage());
		}
		
	}
	
	/**
	 * 生成md5
	 * @param seckilled
	 * @return
	 */
	private String getMD5 (long seckilled){
		String base = seckilled + "/"+slat;
		String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
		return md5;
	}

}
