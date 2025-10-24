package com.xinrui.config;

import com.xinrui.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器，用于处理HTTP请求的JWT认证逻辑
 * 继承自OncePerRequestFilter确保每个请求只过滤一次
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil; // JWT工具类，用于处理令牌的验证和解析
    private final PublicEndpointsConfig publicEndpointsConfig; // 公共端点配置，用于判断哪些请求不需要认证

    /**
     * 构造函数，注入JWT工具类和公共端点配置
     * @param jwtUtil JWT工具类实例
     * @param publicEndpointsConfig 公共端点配置实例
     */
    public JwtAuthenticationFilter(JwtUtil jwtUtil, PublicEndpointsConfig publicEndpointsConfig) {
        this.jwtUtil = jwtUtil;
        this.publicEndpointsConfig = publicEndpointsConfig;
    }

    /**
     * 核心过滤方法，处理每个请求的认证逻辑
     * @param request HTTP请求对象
     * @param response HTTP响应对象
     * @param filterChain 过滤器链
     * @throws ServletException Servlet异常
     * @throws IOException IO异常
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // 跳过公共端点和OPTIONS预检请求
        if (publicEndpointsConfig.isPublicEndpoint(requestURI) || "OPTIONS".equalsIgnoreCase(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        // 检查请求头是否存在且以"Bearer "开头
        if (header != null && header.startsWith("Bearer ")) {
            // 提取JWT令牌（去掉"Bearer "前缀）
            String token = header.substring(7);
            // 验证令牌的有效性
            try {
                if (jwtUtil.validateToken(token)) {
                    // 解析令牌获取声明信息
                    Claims claims = jwtUtil.parseToken(token);
                    // 从声明中获取用户名
                    String username = claims.getSubject();
                    // 从声明中获取用户角色
                    String role = claims.get("role", String.class);

                    // 创建认证对象，设置用户名和角色权限
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null,
                                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)));

                    // 将认证信息设置到安全上下文中
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    // 继续过滤器链
                    filterChain.doFilter(request, response);
                    return;
                } else {
                    // Token无效，返回401错误
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Invalid token");
                    return;
                }
            } catch (Exception e) {
                // Token解析异常，返回401错误
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid token: " + e.getMessage());
                return;
            }
        } else {
            // 没有提供Token，返回401错误
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing token");
            return;
        }
    }
}