package com.fenglei.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fenglei.common.constant.AuthConstants;
import com.nimbusds.jose.JWSObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class RequestUtils {


    public static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    @SneakyThrows
    public static JSONObject getJwtPayload() {
        String token = getRequest().getHeader(AuthConstants.AUTHORIZATION_KEY);
        token = token.replace(AuthConstants.AUTHORIZATION_PREFIX, Strings.EMPTY);
        JWSObject jwsObject = JWSObject.parse(token);
        String jwtPayload = jwsObject.getPayload().toString();
        JSONObject jsonObject = JSONUtil.parseObj(jwtPayload);
        return jsonObject;
    }

    public static String getUserId() {
        String id = getJwtPayload().getStr(AuthConstants.USER_ID_KEY);
        if (StringUtils.isEmpty(id)){
            return "1";
        }
        return id;
    }

    public static String getUsername() {
        String username = getJwtPayload().getStr(AuthConstants.USER_NAME_KEY);
        if (StringUtils.isEmpty(username)){
            return "超级管理员";
        }
        String decode = null;
        try {
            decode = URLDecoder.decode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    public static String getNickname() {
        String username = getJwtPayload().getStr(AuthConstants.USER_NICK_NAME_KEY);
        if (StringUtils.isEmpty(username)){
            return "超级管理员";
        }
        String decode = null;
        try {
            decode = URLDecoder.decode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return decode;
    }

    /**
     * 获取JWT的载体中的clientId
     *
     * @return
     */
    public static String getClientId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 从请求路径中获取
        String clientId = request.getParameter(AuthConstants.CLIENT_ID_KEY);
        if (StrUtil.isNotBlank(clientId)) {
            return clientId;
        }

        // 从请求头获取
        String basic = request.getHeader(AuthConstants.AUTHORIZATION_KEY);
        if (StrUtil.isNotBlank(basic) && basic.startsWith(AuthConstants.BASIC_PREFIX)) {
            basic = basic.replace(AuthConstants.BASIC_PREFIX, Strings.EMPTY);
            String basicPlainText = new String(Base64.getDecoder().decode(basic.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            clientId = basicPlainText.split(":")[0]; //client:secret
        }
        return clientId;
    }

    /**
     * 获取登录认证的客户端ID
     * <p>
     * 兼容两种方式获取Oauth2客户端信息（client_id、client_secret）
     * 方式一：client_id、client_secret放在请求路径中
     * 方式二：放在请求头（Request Headers）中的Authorization字段，且经过加密，例如 Basic Y2xpZW50OnNlY3JldA== 明文等于 client:secret
     *
     * @return
     */
    @SneakyThrows
    public static String getAuthClientId() {
        String clientId;

        HttpServletRequest request = getRequest();

        // 从请求路径中获取
        clientId = request.getParameter(AuthConstants.CLIENT_ID_KEY);
        if (StrUtil.isNotBlank(clientId)) {
            return clientId;
        }

        // 从请求头获取
        String basic = request.getHeader(AuthConstants.AUTHORIZATION_KEY);
        basic = request.getHeader(AuthConstants.AUTHORIZATION_KEY);
        if (StrUtil.isNotBlank(basic) && basic.startsWith(AuthConstants.BASIC_PREFIX)) {
            basic = basic.replace(AuthConstants.BASIC_PREFIX, Strings.EMPTY);
            String basicPlainText = new String(new BASE64Decoder().decodeBuffer(basic), "UTF-8");
            clientId = basicPlainText.split(":")[0]; //client:secret
        }
        return clientId;
    }


    public static List<String> getRoleIds() {
        List<String> list = getJwtPayload().get(AuthConstants.JWT_AUTHORITIES_KEY, List.class);
        if (null == list){
            return new ArrayList<>();
        }
        List<String> authorities = list.stream().map(String::valueOf).collect(Collectors.toList());
        return authorities;
    }
}
