package com.wwdui.springboot3_modle.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wwdui.springboot3_modle.pojo.LoginUser;
import com.wwdui.springboot3_modle.util.JwtUtil;
import com.wwdui.springboot3_modle.util.RedisCache;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 获取 token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response); // 放行（可能后续有匿名访问逻辑）
            return;
        }

        try {
            // 解析 token
            Claims claims = JwtUtil.parseJWT(token);
            String userid = claims.getSubject();

            // 从 Redis 获取用户信息
            String redisKey = "login:" + userid;
            LoginUser loginUser = redisCache.getCacheObject(redisKey);

            if (loginUser == null) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "用户未登录");
                return;
            }

            if (!loginUser.getToken().equals(token)) {
                sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "请重新登录");
                return;
            }

            // 存入 SecurityContextHolder
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            logger.info("Authentication set successfully for user: " + loginUser.getUsername());
            filterChain.doFilter(request, response); // 放行

        } catch (Exception e) {
            // 捕获 JWT 解析异常、Redis 异常等
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "token非法或已过期");
        }
    }
    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
