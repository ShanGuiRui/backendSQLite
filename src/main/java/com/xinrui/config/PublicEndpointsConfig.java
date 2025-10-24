package com.xinrui.config;

import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Configuration
public class PublicEndpointsConfig {

    private final List<Pattern> publicEndpointPatterns;

    public PublicEndpointsConfig() {
        // 硬编码公共端点，您可以根据需要添加更多
        this.publicEndpointPatterns = Arrays.asList(
            Pattern.compile("/api/auth/.*"), // 认证相关端点
            Pattern.compile("/swagger-ui/.*"), // Swagger UI
            Pattern.compile("/v3/api-docs/.*"), // API 文档
            Pattern.compile("/swagger-resources/.*"), // Swagger 资源
            Pattern.compile("/webjars/.*"), // Webjars
            Pattern.compile("/error") // 错误端点
        );
    }

    public boolean isPublicEndpoint(String requestURI) {
        for (Pattern pattern : publicEndpointPatterns) {
            if (pattern.matcher(requestURI).matches()) {
                return true;
            }
        }
        return false;
    }
}