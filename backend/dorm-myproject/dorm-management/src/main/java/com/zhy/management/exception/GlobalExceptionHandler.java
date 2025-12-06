package com.zhy.management.exception;

import com.zhy.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 统一异常处理类
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    
    //处理异常
    @ExceptionHandler
    public Result ex(Exception e){//方法形参中指定能够处理的异常类型
        e.printStackTrace();//打印堆栈中的异常信息
        //捕获到异常之后，响应一个标准的Result
        return Result.error("对不起,操作失败,请联系管理员");
    }

    /**
     * 数据重复插入捕获
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result handleSQLIntegrityConstraintViolationException(DuplicateKeyException e) {
        String message = e.getMessage();
        log.warn("数据库唯一键冲突: {}", message);

        // 正则匹配：Duplicate entry 'xxx' for key 'yyy'
        Pattern pattern = Pattern.compile("Duplicate entry '(.*?)' for key '(.*?)'");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            String duplicateValue = matcher.group(1); // 如 '197-1'
            String keyName = matcher.group(2);        // 如 'student.uk_room_bed'

            // 判断是否是宿舍床铺唯一索引冲突
            if ("student.uk_room_bed".equals(keyName)) {
                // 从 '197-1' 中提取床号（- 后面的部分）
                int dashIndex = duplicateValue.lastIndexOf('-');
                String bedNo = dashIndex != -1 ? duplicateValue.substring(dashIndex + 1) : duplicateValue;
                return Result.error("床铺 " + bedNo + " 已被占用，请选择其他床位");
            }

            // 其他唯一键冲突（如学号、手机号等）
            return Result.error("数据 [" + duplicateValue + "] 已存在，不能重复添加");
        }

        // 如果正则没匹配上（兼容老版本或其他数据库）
        return Result.error("操作失败：数据已存在");
    }

    //JSR303参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidationExceptions(MethodArgumentNotValidException ex) {
        // 获取第一个错误信息（也可返回全部）
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getDefaultMessage())
                .findFirst()
                .orElse("参数校验失败");

        return Result.error(errorMsg);
    }

    /**
     * 处理业务异常（你抛的 BusinessException）
     */
    @ExceptionHandler(BusinessException.class)
    public Result handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

}