package com.xinrui.service;


public interface IUsersManagerSVC {

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    String login(String username, String password);

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    boolean register(String username, String password);
}
