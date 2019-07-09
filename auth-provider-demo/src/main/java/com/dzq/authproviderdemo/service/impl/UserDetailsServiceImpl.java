package com.dzq.authproviderdemo.service.impl;

import com.dzq.authproviderdemo.domain.User;
import com.dzq.authproviderdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("userDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.findByUsername(username);

        if (user == null) {
            return null;
        }

        // 用户控制属性
        boolean accountNonLocked = true;
        boolean credentialsNonExpired = true;
        boolean enable = true;
        boolean accountNonExpired = true;

        return user;
    }
}
