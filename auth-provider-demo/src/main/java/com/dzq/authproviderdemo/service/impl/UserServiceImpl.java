package com.dzq.authproviderdemo.service.impl;

import com.dzq.authproviderdemo.dao.UserDao;
import com.dzq.authproviderdemo.domain.User;
import com.dzq.authproviderdemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
