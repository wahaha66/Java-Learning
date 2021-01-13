package com.david.springboot.service;

import com.david.springboot.bean.model.User;
import com.david.springboot.dao.p.UserMapperPrimary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserMapperPrimary userDao;

    public User selectUserByName(String name) {
        return userDao.findUserByName(name);
    }

    public void insertUser(User user){
        userDao.insertUser(user.getName(),user.getAge(),user.getMoney());
    }

    public void updateUser(double money,String name){
        userDao.updateUser(money,name);
    }

    public List<User> findAllUser(){
        return userDao.findAll();
    }

    public void deleteUser(Long userId){
        userDao.deleteUser(userId);
    }
}
