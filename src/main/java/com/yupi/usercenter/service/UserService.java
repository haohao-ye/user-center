package com.yupi.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.usercenter.common.Result;
import com.yupi.usercenter.pojo.User;
import com.yupi.usercenter.pojo.vo.LoginRequst;
import com.yupi.usercenter.pojo.vo.RegistRequst;
import com.yupi.usercenter.pojo.vo.UserSearchRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 86135
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-03-03 20:42:10
 */
public interface UserService extends IService<User> {

    Result<Long> userRegist(RegistRequst registRequst);

    Result<Long> userLogin(LoginRequst loginRequst, HttpServletRequest httpRequest);


    Result<Long> checkUserAcount(String username);


    Result<List<User>> getUserList(UserSearchRequest userSearchRequest);

    User getSafetyUser(User originUser);

    Result<Long> logout(HttpServletRequest request);
}
