package com.xinrui.mapper;

import com.xinrui.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * UserMapper接口 - 用户数据访问层接口
 * 使用@Mapper注解标记为MyBatis的映射器接口
 */
@Mapper
public interface UsersMapper {
    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return 返回匹配的用户对象，如果没有找到则返回null
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    Users findByUsername(String username);

    /**
     * 插入新用户信息
     * @param users 包含用户信息的对象，包含username、password和role属性
     */
    @Select("INSERT INTO users (username, password, role) VALUES (#{username}, #{password}, #{role})")
    void insert(Users users);
}