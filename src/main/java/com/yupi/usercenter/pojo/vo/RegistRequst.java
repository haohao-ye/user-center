package com.yupi.usercenter.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description:
 * @date 2024/3/410:09
 */
@Data
public class RegistRequst implements Serializable {

    private static final long serialVersionUID = 6162025028503530855L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;
}
