package com.david.springboot.service;

import com.david.springboot.bean.model.User;
import com.david.springboot.dao.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    /**
     * 根据名字查找用户
     */
    public User selectUserByName(String name) {
        return userDao.findUserByName(name);
    }
}
