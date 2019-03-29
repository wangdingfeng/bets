package com.simple.bets.core.common.handler;

import com.simple.bets.core.base.model.ResponseResult;
import com.simple.bets.core.common.exception.LimitAccessException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.ExpiredSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局控制 异常
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 权限控制
     *
     * @param request
     * @return
     */
    @ExceptionHandler(value = AuthorizationException.class)
    public Object handleAuthorizationException(HttpServletRequest request) {
        if (isAjaxRequest(request)) {
            logger.error("暂无权限，请联系管理员！");
            return ResponseResult.error("暂无权限，请联系管理员！");
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName("error/403");
            return mav;
        }
    }

    /**
     * 运行时异常
     * @param exception
     * @return
     */
    @ExceptionHandler({ RuntimeException.class })
    @ResponseStatus(HttpStatus.OK)
    public Object processException(RuntimeException exception,HttpServletRequest request) {
        logger.error("500异常,请查看报错信息",exception);
        return getExceptionData(exception,request);

    }

    /**
     * Excepiton异常
     * @param exception
     * @return
     */
    @ExceptionHandler({ Exception.class })
    @ResponseStatus(HttpStatus.OK)
    public Object processException(Exception exception,HttpServletRequest request) {
        logger.error("500异常,请查看报错信息",exception);
        return getExceptionData(exception,request);

    }

    /**
     * 获取错误信息
     * @param exception
     * @param request
     * @return
     */
    private Object getExceptionData(Exception exception,HttpServletRequest request){
        if (isAjaxRequest(request)) {
            return ResponseResult.error("500异常,请联系管理员");
        }else{
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("exception", exception);
            modelAndView.setViewName("error/500");
            return modelAndView;
        }
    }

    /**
     * 全局登录
     *
     * @return
     */
    @ExceptionHandler(value = ExpiredSessionException.class)
    public String handleExpiredSessionException() {
        return "login";
    }

    /**
     * 接口限流
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = LimitAccessException.class)
    public ResponseResult handleLimitAccessException(LimitAccessException e) {
        return ResponseResult.error(e.getMessage());
    }



    private static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

}
