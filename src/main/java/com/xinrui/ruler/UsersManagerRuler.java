package com.xinrui.ruler;

import com.xinrui.entity.Users;
import com.xinrui.manager.c2s.C2sLogin;
import com.xinrui.manager.s2c.S2cLogin;
import com.xinrui.service.IUsersManagerSVC;
import com.xinrui.utils.ApiResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class UsersManagerRuler {
    @Autowired
    private IUsersManagerSVC usersSVC;

    /**
     * 用户登录
     * @param c2sLogin
     * @return
     */
    @PostMapping("/login")
    public ApiResult login(@RequestBody C2sLogin c2sLogin) {
        Users users = new Users();
        BeanUtils.copyProperties(c2sLogin, users);
        String token = usersSVC.login(users.getUsername(), users.getPassword());
        if (token != null) {
            S2cLogin response = new S2cLogin();
            response.setToken(token);
            response.setUsername(users.getUsername());
            return ApiResult.success().setData(response);
        }
        return ApiResult.error("用户名或密码错误");
    }

    /**
     * 用户注册
     * @param c2sLogin
     * @return
     */
    @PostMapping("/register")
    public ApiResult register(@RequestBody C2sLogin c2sLogin) {
        Users users = new Users();
        BeanUtils.copyProperties(c2sLogin, users);
        boolean success = usersSVC.register(users.getUsername(), users.getPassword());
        return success ? ApiResult.success() : ApiResult.error("用户名已存在");
    }
}
