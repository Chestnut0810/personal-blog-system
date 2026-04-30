package com.example.blog.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements org.springframework.web.servlet.HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // 放行公开路径
        if (uri.startsWith("/login") || uri.startsWith("/register") || uri.startsWith("/") ||
                uri.startsWith("/articles") || uri.startsWith("/css") || uri.startsWith("/js")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return false;
        }

        // 如果访问/admin/*路径，确认角色是ADMIN
        if (uri.startsWith("/admin")) {
            Object userObj = session.getAttribute("user");
            if (userObj == null || !"ADMIN".equals(((com.example.blog.entity.User)userObj).getRole())) {
                response.sendRedirect("/");
                return false;
            }
        }

        return true;
    }
}
