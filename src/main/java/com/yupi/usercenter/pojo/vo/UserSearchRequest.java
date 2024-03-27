package com.yupi.usercenter.pojo.vo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description:
 * @date 2024/3/416:28
 */
@Data
public class UserSearchRequest implements Serializable {
    private static final long serialVersionUID = 7047101671331615908L;
    private String username;
    private Integer pageNum = 1;
    private Integer pageSize =10;
}
