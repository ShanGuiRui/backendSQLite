package com.xinrui.service.impl;

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

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public String login(String username, String password) {
        Users users = usersMapper.findByUsername(username);
        if (users != null && passwordEncoder.matches(password, users.getPassword())) {
            return jwtUtil.generateToken(users.getUsername(), users.getRole());
        }
        return null;
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean register(String username, String password) {
        if (usersMapper.findByUsername(username) != null) {
            return false;
        }
        Users users = new Users();
        users.setUsername(username);
        users.setPassword(passwordEncoder.encode(password));
        users.setRole("USER");
        usersMapper.insert(users);
        return true;
    }
}
