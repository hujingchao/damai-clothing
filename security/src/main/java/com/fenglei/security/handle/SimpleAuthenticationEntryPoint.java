package com.fenglei.security.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fenglei.common.result.ResultCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * token异常响应
 * @author 秦
 * @since 2019/11/6 22:11
 */
@Component
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Resource
    private ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        //todo your business
        HashMap<String, String> map = new HashMap<>(4);
        map.put("uri", request.getRequestURI());
        map.put("message", authException.getMessage());
        map.put("date",ResultCode.TOKEN_INVALID_OR_EXPIRED.getMsg());
        map.put("code", ResultCode.TOKEN_INVALID_OR_EXPIRED.getCode());
        response.setHeader("Content-Type", "application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(map));
    }
}