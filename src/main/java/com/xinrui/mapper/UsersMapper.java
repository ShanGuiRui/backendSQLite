package com.xinrui.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xinrui.entity.Users;
import org.apache.ibatis.annotations.Mapper;

/**
 * UserMapper接口 - 用户数据访问层接口
 * 使用@Mapper注解标记为MyBatis的映射器接口
 */
@Mapper
public interface UsersMapper extends BaseMapper<Users> {
}