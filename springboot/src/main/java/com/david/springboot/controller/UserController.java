package com.david.springboot.controller;

import com.david.framework.bean.RespState;
import com.david.springboot.bean.model.User;
import com.david.springboot.bean.so.UserSO;
import com.david.springboot.bean.vo.UserVO;
import com.david.springboot.service.UserService;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Value("${com.david.name}")
    private String name;
    @Resource
    private UserService userService;


    @GetMapping(value = "/")
    public List<UserVO> getUserList() {
        List<User> userList = userService.findAllUser();
        List<UserVO> userVOS = Lists.newArrayList();
        BeanUtils.copyProperties(userList, userVOS);
        return userVOS;
    }

    @PostMapping(value = "/")
    public String postUser(@Valid @RequestBody UserSO userSO) {
        User user = new UserVO();
        BeanUtils.copyProperties(userSO, user);
        userService.insertUser(user);
        return "success";
    }

    @GetMapping("/{id}")
    public RespState delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return new RespState(true);
    }

    @GetMapping("/name/{name}")
    public UserVO getUserByName(@PathVariable String name) {
        UserVO userVO = new UserVO();
        User user = userService.selectUserByName(name);
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @PutMapping("/money/{name}")
    public RespState updateMoney(@PathVariable String name, Double money) {
        userService.updateUser(money,name);
        return new RespState(true);
    }

}
