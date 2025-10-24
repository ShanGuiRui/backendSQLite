package com.xinrui.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private final SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expiration = 86400000; // 24小时

    /**
     * 生成JWT令牌的方法
     * @param username 用户名
     * @param role 用户角色
     * @return 生成的JWT令牌字符串
     */
    public String generateToken(String username, String role) {
    // 使用Jwts.builder()构建JWT令牌
        return Jwts.builder()
            // 设置主题（subject）为用户名
                .setSubject(username)
            // 添加角色声明
                .claim("role", role)
            // 设置签发时间为当前时间
                .setIssuedAt(new Date())
            // 设置过期时间为当前时间加上过期时长
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
            // 使用密钥进行签名
                .signWith(key)
            // 生成并返回紧凑的JWT令牌字符串
                .compact();
    }

    /**
     * 解析JWT令牌并返回其中的声明(payload)
     * @param token JWT格式的令牌字符串
     * @return 解析后的Claims对象，包含令牌中的所有声明信息
     */
    public Claims parseToken(String token) {
    // 使用Jwts.parser()创建解析器
    // 设置验证密钥
    // 构建解析器
    // 解析签名后的声明
    // 返回载荷部分
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 验证令牌的有效性
     * @param token 需要验证的令牌字符串
     * @return 如果令牌有效返回true，否则返回false
     */
    public boolean validateToken(String token) {
        try {
        // 尝试解析令牌，如果解析成功则说明令牌有效
            parseToken(token);
        // 令牌有效，返回true
            return true;
        } catch (Exception e) {
        // 如果解析过程中出现任何异常，说明令牌无效，返回false
            return false;
        }
    }
}