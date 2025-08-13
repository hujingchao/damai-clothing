package com.fenglei.security.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fenglei.model.system.dto.UserDTO;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.security.mapper.UserMapper;
import com.fenglei.security.mapper.UserRoleMapper;
import com.fenglei.security.pojo.CustomerContactUserDetail;
import com.fenglei.security.pojo.User;
import com.fenglei.security.pojo.WechatAuthDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author zhouyiqiu
 * @Date 2021/12/2 15:23
 * @Version 1.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper securityUserMapper;
    @Resource
    private UserRoleMapper securityUserRoleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = securityUserMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getMobile, username));

        if (user == null) {
            throw new UsernameNotFoundException("该用户不存在！");
        }

        // Entity->DTO
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user, userDTO);

        // 获取用户的角色ID集合
        List<String> roleIds = securityUserRoleMapper.selectList(user.getId()).stream().map(item -> item.getRoleId()).collect(Collectors.toList());
        userDTO.setRoleIds(roleIds);
        User userDetails = new User(userDTO);
        return userDetails;
    }

    public CustomerContactUserDetail loadCustomerContactByOpenId(String openId) throws UsernameNotFoundException {
        CustomerContactUserDetail customerContact = securityUserMapper.loadCustomerContactByOpenId(openId);
        return customerContact;
    }

    public CustomerContactUserDetail loadCustomerContactByPhone(String phone) throws UsernameNotFoundException {
        CustomerContactUserDetail customerContact = securityUserMapper.loadCustomerContactByPhone(phone);
        return customerContact;
    }

    public Boolean addCustomerContact(CustomerContactUserDetail customerContact) {
        return securityUserMapper.addCustomerContact(customerContact);
    }

    public WechatAuthDTO getWechatInfoByOpenId(String openId) {
        return securityUserMapper.getWechatInfoByOpenId(openId);
    }

    public Boolean updateCustomerContact(CustomerContactUserDetail contactByPhone) {
        return securityUserMapper.updateCustomerContact(contactByPhone);
    }
}
