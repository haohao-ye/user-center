package com.yupi.usercenter.controller;

import com.yupi.usercenter.common.Result;
import com.yupi.usercenter.common.ResultCodeEnum;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.pojo.User;
import com.yupi.usercenter.pojo.vo.LoginRequst;
import com.yupi.usercenter.pojo.vo.RegistRequst;
import com.yupi.usercenter.pojo.vo.UserSearchRequest;
import com.yupi.usercenter.service.UserService;
import com.yupi.usercenter.util.JwtHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

import static com.yupi.usercenter.constant.UserContant.ADMIN_ROLE;
import static com.yupi.usercenter.constant.UserContant.LOGIN_STATE;

/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description:
 * @date 2024/3/114:54
 */
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    JwtHelper jwtHelper;
    @Autowired
    UserService userService;

    /**
     * 用户登录接口
     *
     * @param loginRequst password&&userAcount
     * @return
     */
    @PostMapping("login")
    public Result<Long> login(@RequestBody LoginRequst loginRequst, HttpServletRequest request) {
        if (loginRequst == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_NULL_ERROR, "参数为空");
        }
        //1 校验
        if (StringUtils.isAnyBlank(loginRequst.getUserAccount(), loginRequst.getUserPassword())) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "账户或密码为空");
        }
        return userService.userLogin(loginRequst, request);

    }

    @PostMapping("logout")
    public Result<Long> logout(HttpServletRequest request) {
        return userService.logout(request);

    }


    @GetMapping("/current")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ResultCodeEnum.NOT_LOGIN, "未登录");
        }
        long userId = currentUser.getId();
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return Result.ok(safetyUser);
    }


    /**
     * 用户名重复检验
     *
     * @param username
     * @return
     */

    @PostMapping("checkUserName")
    public Result<Long> checkUsername(String username) {
        return userService.checkUserAcount(username);
    }

    @PostMapping("regist")
    public Result<Long> regist(@RequestBody RegistRequst registRequst) {
        if (registRequst == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_NULL_ERROR);
        }
        //1 校验
        if (StringUtils.isAnyBlank(registRequst.getUserAccount(), registRequst.getUserPassword(), registRequst.getCheckPassword())) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR);
        }
        return userService.userRegist(registRequst);
    }

    @GetMapping("search")
    public Result<List<User>> searchUserList(@RequestBody UserSearchRequest userSearchRequest, HttpServletRequest request) {
        if (userSearchRequest == null) {
            throw new BusinessException(ResultCodeEnum.PARAMS_NULL_ERROR);
        }
        //授权检验
        if (!isAuthorize(request)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        return userService.getUserList(userSearchRequest);
    }

    @PostMapping("delete")
    public Result<Long> deleteUserById(Long id, HttpServletRequest request) {
        //授权检验
        if (!isAuthorize(request)) {
            throw new BusinessException(ResultCodeEnum.UNAUTHORIZED);
        }
        boolean isRemove = userService.removeById(id);
        if (!isRemove) {
            return Result.fail(null);
        }
        return Result.ok(null);
    }

    boolean isAuthorize(HttpServletRequest request) {

        Object userObj = request.getSession().getAttribute(LOGIN_STATE);
        User user = (User) userObj;
        return user != null && Objects.equals(user.getUserRole(), ADMIN_ROLE);

    }


}
