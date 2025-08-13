package com.fenglei.security.handle;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenglei.common.result.ResultCode;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * 权限认证异常处理
 * @author 秦
 * @since 2019/11/6 22:19
 */
@Component
public class SimpleAccessDeniedHandler implements AccessDeniedHandler {
    @Resource
    private ObjectMapper objectMapper;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        //todo your business
        HashMap<String, String> map = new HashMap<>(4);
        map.put("uri", request.getRequestURI());
        map.put("message", accessDeniedException.getMessage());
        map.put("date", ResultCode.ACCESS_UNAUTHORIZED.getMsg());
        map.put("code",ResultCode.ACCESS_UNAUTHORIZED.getCode());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}
