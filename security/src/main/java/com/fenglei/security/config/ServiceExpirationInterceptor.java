package com.fenglei.security.config;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.fenglei.common.constant.AdminConstants;
import com.fenglei.common.constant.GlobalConstants;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.model.system.entity.SysMenu;
import com.fenglei.model.system.vo.RouterVO;
import com.fenglei.security.mapper.UserMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ServiceExpirationInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    /**
     * 预处理回调方法，实现处理器预处理
     * 返回值：true表示继续流程；false表示流程中断，不会继续调用其他的拦截器或者处理器
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        userMapper.insertInterceptorLog(request.getMethod(), request.getRequestURI(), request.getQueryString(), RequestUtils.getUserId());
//
//        userMapper.selectInterceptorLogs();
//
//        List<SysMenu> menuList = userMapper.listForRouter();
//        List<RouterVO> list = recursionForRoutes(AdminConstants.ROOT_MENU_ID, menuList);

        return true;
    }

    // 递归生成路由
    private List<RouterVO> recursionForRoutes(String parentId, List<SysMenu> menuList) {
        List<RouterVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).ifPresent(menus -> menus.stream().filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    RouterVO routerVO = new RouterVO();
                    routerVO.setName(menu.getName());
                    if (AdminConstants.ROOT_MENU_ID.equals(parentId)) {
                        routerVO.setPath(menu.getPath());
                        routerVO.setComponent("Layout");
                    } else {
                        routerVO.setPath(menu.getPath());// 显示在地址栏上
                        routerVO.setComponent(menu.getComponent());
                    }
                    routerVO.setRedirect(menu.getRedirect());
                    routerVO.setMeta(routerVO.new Meta(
                            menu.getTitle(),
                            menu.getKeepAlive(),
                            menu.getIsTitle(),
                            menu.getIcon(),
                            menu.getRoles()
                    ));
                    // 菜单显示隐藏
                    routerVO.setHidden(!GlobalConstants.VISIBLE_SHOW_VALUE.equals(menu.getVisible()) ? true : false);
                    List<RouterVO> children = recursionForRoutes(menu.getId(), menuList);
                    routerVO.setChildren(children);
                    if (CollectionUtil.isNotEmpty(children)) {
                        routerVO.setAlwaysShow(Boolean.TRUE); // 显示子节点
                    }
                    list.add(routerVO);
                }));
        return list;
    }

}
