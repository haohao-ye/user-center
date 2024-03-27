package com.yupi.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.usercenter.common.Result;
import com.yupi.usercenter.common.ResultCodeEnum;
import com.yupi.usercenter.exception.BusinessException;
import com.yupi.usercenter.mapper.UserMapper;
import com.yupi.usercenter.pojo.User;
import com.yupi.usercenter.pojo.vo.LoginRequst;
import com.yupi.usercenter.pojo.vo.RegistRequst;
import com.yupi.usercenter.pojo.vo.UserSearchRequest;
import com.yupi.usercenter.service.UserService;
import com.yupi.usercenter.util.JwtHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.yupi.usercenter.constant.UserContant.LOGIN_STATE;

/**
 * @author 86135
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-03-03 20:42:10
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    static final String SALT = "dkhaohao";
    /**
     *
     */
    @Autowired
    JwtHelper jwtHelper;

    @Resource
    UserMapper userMapper;

    /**
     * @param registRequst 请求参数
     * @return
     */
    @Override
    public Result<Long> userRegist(RegistRequst registRequst) {
        //不查数据库的先校验
        //账户不能包含特殊字符
        checkUserParams(registRequst.getUserAccount(), registRequst.getUserPassword());

        if (!registRequst.getCheckPassword().equals(registRequst.getUserPassword())) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "两次密码不相等");
        }

        //账户不能重复（包含查询数据库的语句放最后校验）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!(StringUtils.isBlank(registRequst.getUserAccount())), "user_account", registRequst.getUserAccount());
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ResultCodeEnum.ACCOUNT_USED, "账号已被使用");
        }

        //2 加密

        String encryptedPwd = DigestUtils.md5DigestAsHex(StringUtils.join(SALT, registRequst.getUserPassword()).getBytes());

        //3 插入数据
        User user = new User();
        user.setUserAccount(registRequst.getUserAccount());
        user.setUserPassword(encryptedPwd);
        int row = userMapper.insert(user);
        if (row <= 0) {
            return Result.fail(null);
        }

        return Result.ok(user.getId());
    }

    @Override
    public Result<Long>  userLogin(LoginRequst loginRequst, HttpServletRequest request) {

        //不查数据库的先校验
        //账户不能包含特殊字符
        checkUserParams(loginRequst.getUserAccount(), loginRequst.getUserPassword());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_account", loginRequst.getUserAccount());
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            //用户不存在

            log.info("user login failed: user can`t find");
            return Result.build(null, ResultCodeEnum.ACCOUNT_ERROR, "账户不存在");
        }

        //2 加密

        String encryptedPwd = DigestUtils.md5DigestAsHex(StringUtils.join(SALT, loginRequst.getUserPassword()).getBytes());
        if (!encryptedPwd.equals(user.getUserPassword())) {
            //密码错误
            return Result.build(null, ResultCodeEnum.PASSWORD_ERROR, "密码错误");
        }
        //保存用户登录状态
        HttpSession session = request.getSession();
        session.setAttribute(LOGIN_STATE, user);
        return Result.ok(null);
    }


    public Result<Long>  checkUserAcount(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, username);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            return Result.build(null, ResultCodeEnum.ACCOUNT_USED);
        }
        return Result.ok(null);
    }

    @Override
    public Result getUserList(UserSearchRequest userSearchRequest) {
        Page<User> userPage = new Page<>(userSearchRequest.getPageNum(), userSearchRequest.getPageSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(userSearchRequest.getUsername()), User::getUsername, userSearchRequest.getUsername());
        userMapper.selectPage(userPage, queryWrapper);
        Map<String, Object> pageInfo = new HashMap<>();
        //数据脱敏
        List<User> userList = userPage.getRecords().stream().map(
                user -> {
                    return this.getSafetyUser(user);
                }
        ).collect(Collectors.toList());
        pageInfo.put("pageData", userList);
        pageInfo.put("pageNum", userPage.getCurrent());
        pageInfo.put("pageSize", userPage.getSize());
        pageInfo.put("totalPage", userPage.getPages());
        pageInfo.put("totalSize", userPage.getTotal());
        Map<String,Object> data = new HashMap();
        data.put("pageInfo", pageInfo);
        return Result.ok(data);

    }

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    @Override
    public User getSafetyUser(User originUser) {
        if (originUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

    @Override
    public Result logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute(LOGIN_STATE);
        return Result.ok(null);
    }

    public void checkUserParams(String userAccount, String password) {
        String regEx = "\\s";

        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "账号包含空字符");
        }

        if (userAccount.length() < 6) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "账号字数小于6");
        }

        if (password.length() < 6) {
            throw new BusinessException(ResultCodeEnum.PARAMS_ERROR, "密码字数小于6");
        }

    }


}




