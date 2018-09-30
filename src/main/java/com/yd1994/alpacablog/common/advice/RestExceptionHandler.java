package com.yd1994.alpacablog.common.advice;

import com.yd1994.alpacablog.common.exception.ResourceNotFoundException;
import com.yd1994.alpacablog.common.exception.TableVersionNotFoundException;
import com.yd1994.alpacablog.common.exception.VersionNotFoundException;
import com.yd1994.alpacablog.common.result.ResultFactory;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.NoSuchElementException;

/**
 * 异常统一处理
 *
 * @author yd
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * http状态码参考：
     * <p>
     * 1**： 信息，服务器收到请求，需要请求者继续执行操作
     * 2**： 成功，操作被成功接收并处理
     * 3**： 重定向，需要进一步的操作以完成请求
     * 4**： 客户端错误，请求包含语法错误或无法完成请求
     * 5**： 服务器错误，服务器在处理请求的过程中发生了错误
     * <p>
     * <p>
     * 200： 请求成功。
     * 400： 客户端请求的语法错误，服务器无法理解
     * 401： 请求要求用户的身份认证
     * 403： 请求的用户没有资源权限
     * 404： 服务器无法根据客户端的请求找到资源
     * 410： 资源不可以或已经被移除
     * 500： 服务器内部错误
     * 503： 服务器已关闭或无法接收和处理请求
     */

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 资源不存在异常 统一处理
     * 返回 404 错误码
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {ResourceNotFoundException.class, NoSuchElementException.class, EmptyResultDataAccessException.class})
    public ResultFactory.Info sourceNotFoundException(NativeWebRequest request, Exception ex) {
        logger.debug(ex.getMessage());
        return ResultFactory.get404Info();
    }

    /**
     * 数据库的表中 乐观锁：version 为null异常 统一处理
     * 返回 500 错误码
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = TableVersionNotFoundException.class)
    public ResultFactory.Info tableVersionNotFoundException(NativeWebRequest request, TableVersionNotFoundException ex) {
        logger.error(ex.getMessage());
        return ResultFactory.get500Info();
    }

    /**
     * 数据已经被修改：乐观锁不一致 异常统一处理
     * 返回 400 错误码
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = StaleObjectStateException.class)
    public ResultFactory.Info staleObjectStateException(NativeWebRequest request, StaleObjectStateException ex) {
        logger.error(ex.getMessage());
        return ResultFactory.get400Info().appendMessage("该资源已经被修改。");
    }

    /**
     * 乐观锁：version为null异常统一处理
     * 返回 400 错误码
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(value = VersionNotFoundException.class)
    public ResultFactory.Info versionNotFoundException(NativeWebRequest request, VersionNotFoundException ex) {
        logger.info(ex.getMessage());
        return ResultFactory.get400Info().appendMessage(ex.getMessage());
    }

}
