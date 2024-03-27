package com.yupi.usercenter.common;

import java.io.Serializable;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description: 全局统一返回结果类
 * @date 2024/3/114:32
 */
public class Result<T> implements Serializable {
    private static final long serialVersionUID = 5879247796952669250L;
    // 返回码
    private Integer code;
    // 返回消息
    private String message;
    // 返回数据
    private T data;

    private String description;

    /**
     *
     */
    public Result() {
        super();

    }

    // 返回数据
    protected static <T> Result<T> build(T data) {
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> build(T body, Integer code, String message) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        result.setDescription("");
        return result;
    }
    public static <T> Result<T> build(T body, Integer code, String message,String description) {
        Result<T> result = build(body);
        result.setCode(code);
        result.setMessage(message);
        result.setDescription(description);
        return result;
    }

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        result.setDescription(resultCodeEnum.getDescription());
        return result;
    }
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum,String description) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        result.setDescription(description);
        return result;
    }

    /**
     * 操作成功
     *
     * @param data baseCategory1List
     * @param <T>
     * @return
     */
    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    public static <T> Result<T> fail(T data) {
        return build(data, ResultCodeEnum.SYSTEM_ERROR);
    }

    public static <T> Result<T> fail(T data,ResultCodeEnum resultCodeEnum) {
        return build(data, resultCodeEnum);
    }
    public static <T> Result<T> fail(T data,Integer code,String message,String description) {
        return build(data, code,message,description);
    }
    public static <T> Result<T> fail(T data,Integer code,String message) {
        return build(data, code,message,"");
    }

    public Result<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}