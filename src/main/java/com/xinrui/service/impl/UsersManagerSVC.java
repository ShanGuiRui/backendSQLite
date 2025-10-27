package com.xinrui.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xinrui.entity.Users;
import com.xinrui.mapper.UsersMapper;
import com.xinrui.service.IUsersManagerSVC;
import com.xinrui.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersManagerSVC implements IUsersManagerSVC {
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /// 使用Spring Security提供的BCryptPasswordEncoder提供的密码服务：
    /// 将users.setPassword(password);替换为users.setPassword(passwordEncoder.encode(password));
    /// 将password.equals(users.getPassword())替换为passwordEncoder.matches(password, users.getPassword())

    /**
     * 用户登录
     */
    @Override
    public String login(String username, String password) {
        // 使用QueryWrapper构建查询条件
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Users users = usersMapper.selectOne(queryWrapper);
        if (users != null && password.equals(users.getPassword())) {
            return jwtUtil.generateToken(users.getUsername(), users.getRole());
        }
        return null;
    }

    /**
     * 用户注册
     */
    @Override
    public boolean register(String username, String password) {
        // 使用QueryWrapper检查用户是否存在
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        Long count = usersMapper.selectCount(queryWrapper);

        if (count > 0) {
            return false;
        }
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(password);
        users.setRole("USER");
    return usersMapper.insert(users) > 0;
    }
}
