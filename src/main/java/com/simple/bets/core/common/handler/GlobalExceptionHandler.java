package com.simple.bets.core.common.handler;

import com.simple.bets.core.common.exception.FileDownloadException;
import com.simple.bets.core.common.exception.LimitAccessException;
import com.simple.bets.core.model.ResponseResult;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.session.ExpiredSessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
     * 全局登录
     * @return
     */
    @ExceptionHandler(value = ExpiredSessionException.class)
    public String handleExpiredSessionException() {
        return "login";
    }

    @ExceptionHandler(value = LimitAccessException.class)
    public ResponseResult handleLimitAccessException(LimitAccessException e) {
        return ResponseResult.error(e.getMessage());
    }

    @ExceptionHandler(value = FileDownloadException.class)
    public ResponseResult handleFileDownloadException(FileDownloadException e) {
        return ResponseResult.error(e.getMessage());
    }

    private static boolean isAjaxRequest(HttpServletRequest request) {
        return (request.getHeader("X-Requested-With") != null
                && "XMLHttpRequest".equals(request.getHeader("X-Requested-With")));
    }

}
