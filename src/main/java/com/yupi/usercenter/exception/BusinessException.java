package com.yupi.usercenter.exception;

import com.yupi.usercenter.common.ResultCodeEnum;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description:
 * @date 2024/3/50:04
 */
public class BusinessException extends RuntimeException{
    private int code;
    private String description;

    public BusinessException(int code,String message,String description) {
        super(message);
        this.code = code;
        this.description=description;
    }

    public BusinessException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code=resultCodeEnum.getCode();
        this.description=resultCodeEnum.getDescription();
    }

    public BusinessException(ResultCodeEnum resultCodeEnum,String description) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
