package com.david.springboot.service;

import com.david.springboot.bean.model.User;
import com.david.springboot.dao.p.UserMapperPrimary;
import com.david.springboot.dao.s.UserMapperSecondary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserPrimaryService {

    @Resource
    private UserMapperPrimary userMapperPrimary;
    @Resource
    private UserMapperSecondary userMapperSecondary;

    public User getUserByPrimary(String userName) {
        // 从Primary数据源查询刚才插入的数据，配置正确就可以查询到
        return userMapperPrimary.findByName(userName);
    }

    public User getUserBySecondary(String userName) {
        // 从Primary数据源查询刚才插入的数据，配置正确就可以查询到
        return userMapperSecondary.findByName(userName);
    }

}
