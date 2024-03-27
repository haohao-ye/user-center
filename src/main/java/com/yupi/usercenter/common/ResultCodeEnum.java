package com.yupi.usercenter.common;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description: 统一返回结果状态信息类
 * @date 2024/3/114:33
 */
public enum ResultCodeEnum {

    /**
     * The operation was successful
     */
    SUCCESS(20000,"success",""),

    /**
     * 用户参数错误
     */
    PARAMS_ERROR(40000,"paramsError",""),
    /**
     * 用户参数为null
     */
    PARAMS_NULL_ERROR(40001, "paramsNull",""),

    /**
     * username error
     */
    ACCOUNT_ERROR(40601,"userAccountError",""),
    /**
     * password error
     */
    PASSWORD_ERROR(40603,"passwordError",""),
    /**
     * Not logged in
     */
    NOT_LOGIN(40100,"notLogin",""),
    /**
     * duplicated userAcount
     */
    ACCOUNT_USED(40600,"userAccountUsed",""),
    /**
     * 信息为空
     */
    MESSAGE_NULL(50006,"messageNull",""),
    /**
     * 无权访问
     */
    UNAUTHORIZED(403000,"forbidden",""),

    /**
     * 操作失败
     */
    SYSTEM_ERROR(50000,"fail","");

    private Integer code;
    private String message;
    private  String description;
    private ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    private ResultCodeEnum(Integer code,String message,String description){
        this.code=code;
        this.message=message;
        this.description=description;
    }
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    public String getDescription(){
        return  description;
    }
}