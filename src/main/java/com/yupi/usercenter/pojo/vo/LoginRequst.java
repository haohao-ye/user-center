package com.yupi.usercenter.pojo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description:
 * @date 2024/3/415:45
 */
@Data
public class LoginRequst implements Serializable {
    private static final long serialVersionUID = -8775292639986492141L;
    private String userAccount;
    private String userPassword;
}
