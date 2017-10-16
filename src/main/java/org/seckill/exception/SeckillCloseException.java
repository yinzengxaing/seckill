package org.seckill.exception;

/**
 * 秒杀业务员关闭异常
 * @author Hp
 *
 */
public class SeckillCloseException extends SeckillException {
	
	private static final long serialVersionUID = 1L;

	public SeckillCloseException(String message) {
		super(message);
	}
	
	public SeckillCloseException(String message,Throwable cause){
		super(message,cause);
	}
	

}
