package com.yupi.usercenter.service;


import com.yupi.usercenter.common.Result;
import com.yupi.usercenter.pojo.User;
import com.yupi.usercenter.pojo.vo.RegistRequst;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author dkhaohao
 * @Title:
 * @Package
 * @Description:
 * @date 2024/3/321:05
 */
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserService userService;

    @Test
    void addUser() {
        User user = new User();
        user.setUserAccount("dkhaohao");
        user.setUserPassword("123456");
        user.setUsername("haohao");
        user.setAvatarUrl("aaa");
        user.setGender(0);
        user.setPhone("123456");
        user.setEmail("123@email.com");
        boolean save = userService.save(user);
        System.out.println(user.getId());
        Assertions.assertTrue(save);


    }

    @Test
    void userRegist() {
        RegistRequst registRequst =null;
        Result result = userService.userRegist(registRequst);
        Assertions.assertEquals(999,result.getCode());
        registRequst =new RegistRequst();
        registRequst.setUserAccount("dkhaohao");
        registRequst.setUserPassword("123456");
        registRequst.setCheckPassword("123456");
        Assertions.assertEquals(999,result.getCode());
        registRequst.setUserAccount("dkhao");
        Result result1 = userService.userRegist(registRequst);
        Assertions.assertEquals(999,result1.getCode());
        registRequst.setUserAccount("dkhao hao1");
        Result result2=userService.userRegist(registRequst);
        Assertions.assertEquals(999,result2.getCode());

        registRequst.setUserAccount("dkhaohao3");

        Result result3 = userService.userRegist(registRequst);
        Assertions.assertEquals(200,result3.getCode());

    }
}