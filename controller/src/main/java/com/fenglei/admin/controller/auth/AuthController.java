package com.fenglei.admin.controller.auth;

import com.fenglei.common.constant.AuthConstants;
import com.fenglei.common.result.Result;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.service.system.ISysWechatAuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description 认证中心
 * @Author zhouyiqiu
 * @Date 2021/12/2 13:37
 * @Version 1.0
 */
@Api(tags = "认证中心")
@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
@Slf4j
public class AuthController {

    private TokenEndpoint tokenEndpoint;

    private ISysWechatAuthService wechatAuthService;

    private RedisTemplate redisTemplate;

    @ApiOperation(value = "OAuth2认证", notes = "login")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "grant_type", defaultValue = "password", value = "授权模式", required = true),
            @ApiImplicitParam(name = "client_id", defaultValue = "client", value = "Oauth2客户端ID", required = true),
            @ApiImplicitParam(name = "client_secret", defaultValue = "123456", value = "Oauth2客户端秘钥", required = true),
            @ApiImplicitParam(name = "refresh_token", value = "刷新token"),
            @ApiImplicitParam(name = "username", defaultValue = "admin", value = "登录用户名"),
            @ApiImplicitParam(name = "password", defaultValue = "123456", value = "登录密码"),

            // 微信小程序认证参数（无小程序可忽略）
            @ApiImplicitParam(name = "code", value = "小程序授权code"),
            @ApiImplicitParam(name = "encryptedData", value = "包括敏感数据在内的完整用户信息的加密数据"),
            @ApiImplicitParam(name = "iv", value = "加密算法的初始向量"),
    })
    @GetMapping("/token")
    public OAuth2AccessToken postAccessToken(
            @ApiIgnore Principal principal,
            @ApiIgnore @RequestParam Map<String, String> parameters
    ) throws HttpRequestMethodNotSupportedException {
        return tokenEndpoint.postAccessToken(principal, parameters).getBody();
    }



    @DeleteMapping("/logout")
    public Result logout() {
        cn.hutool.json.JSONObject jsonObject = RequestUtils.getJwtPayload();
        String jti = jsonObject.getStr(AuthConstants.JWT_JTI); // JWT唯一标识
        long exp = jsonObject.getLong(AuthConstants.JWT_EXP); // JWT过期时间戳

        long currentTimeSeconds = System.currentTimeMillis() / 1000;

        if (exp < currentTimeSeconds) { // token已过期，无需加入黑名单
            return Result.success();
        }
        redisTemplate.opsForValue().set(AuthConstants.TOKEN_BLACKLIST_PREFIX + jti, null, (exp - currentTimeSeconds), TimeUnit.SECONDS);
        return Result.success();
    }

    @ApiOperation(value = "获取微信用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "code", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "encryptedData", value = "encryptedData", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "iv", value = "iv", paramType = "query", dataType = "String")
    })
    @PostMapping("/getWechatUserInfo")
    public Result getWechatUserInfo(@RequestParam("code") String code,
                                    @RequestParam("encryptedData") String encryptedData,
                                    @RequestParam("iv") String iv
    ) {
        return Result.success(wechatAuthService.getWechatUserInfo(code, encryptedData, iv));
    }
}
