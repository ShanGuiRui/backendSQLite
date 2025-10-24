package com.xinrui.config;

import com.xinrui.utils.CorsUtil;
import com.xinrui.utils.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Security配置类
 * 用于配置应用程序的安全策略，包括JWT认证、CORS支持和访问控制规则
 */
@Configuration
public class SecurityConfig {
    // JWT工具类，用于处理JWT令牌的生成、验证等操作
    private final JwtUtil jwtUtil;
    // CORS工具类，用于处理跨域资源共享相关配置
    private final CorsUtil corsUtil;
    // 公开端点配置类，用于定义不需要认证的公开API端点
    private final PublicEndpointsConfig publicEndpointsConfig;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    /**
     * 构造函数，通过依赖注入获取所需的服务实例
     * @param jwtUtil JWT工具类实例
     * @param corsUtil CORS工具类实例
     * @param publicEndpointsConfig 公开端点配置类实例
     */
    public SecurityConfig(JwtUtil jwtUtil, CorsUtil corsUtil,
                         PublicEndpointsConfig publicEndpointsConfig,
                         JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.corsUtil = corsUtil;
        this.publicEndpointsConfig = publicEndpointsConfig;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
    }

    /**
     * 配置Spring Security的过滤器链
     * @param http HttpSecurity实例，用于配置安全设置
     * @return 配置好的SecurityFilterChain实例
     * @throws Exception 可能抛出的安全配置异常
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 禁用CSRF保护，因为是无状态的JWT认证
        http.csrf(csrf -> csrf.disable())
            // 启用CORS支持
            .cors(cors -> cors.configurationSource(corsUtil.corsConfigurationSource()))
            // 设置会话管理为无状态模式，不创建会话
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // 配置请求授权规则
            .authorizeHttpRequests(auth -> auth
                // 允许OPTIONS请求（预检请求）
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 允许认证相关的端点
                .requestMatchers("/api/auth/**").permitAll()
                // 允许Swagger相关端点
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
                // 允许错误端点
                .requestMatchers("/error").permitAll()
                // 其他所有请求都需要认证
                .anyRequest().authenticated()
            )
            // 设置自定义的认证入口点
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            )
            // 添加JWT认证过滤器，并将其放在用户名密码认证过滤器之前
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, publicEndpointsConfig),
                    UsernamePasswordAuthenticationFilter.class);

        // 构建并返回安全过滤器链
        return http.build();
    }
}