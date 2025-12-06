package com.zhy.management.exception;

/**
 * 业务异常（运行时异常）
 */
public class BusinessException extends RuntimeException {

    private Integer code; // 可选：错误码

    public BusinessException(String message) {
        super(message);
        this.code = 500; // 默认500
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    // getter
    public Integer getCode() {
        return code;
    }
}