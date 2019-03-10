package com.simple.bets.core.common.exception;

/**
 * @ProjectName: bets
 * @Package: com.simple.bets.core.common.exception
 * @ClassName: ServiceException
 * @Author: wangdingfeng
 * @Description: 自定义service异常
 * @Date: 2019/3/10 10:31
 * @Version: 1.0
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}