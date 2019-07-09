package com.dzq.authproviderdemo.service;

import com.dzq.authproviderdemo.domain.User;

/**
 * @Author dzq
 * @Date  2019/7/9 15:47
 **/
public interface UserService {

    /**
     * @Author dzq
     * @Date  2019/7/9 15:50
     **/
    User findByUsername(String username);
}
