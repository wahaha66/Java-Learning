package com.david.springboot.controller;

import com.david.springboot.bean.model.User;
import com.david.springboot.bean.so.UserSO;
import com.david.springboot.bean.vo.UserVO;
import com.david.springboot.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Value("${com.david.name}")
    private String name;
    @Resource
    private UserService userService;

    /**
     * 创建线程安全的Map,存放用户数据
     */
    private static Map<Long, UserVO> users = Collections.synchronizedMap(new HashMap<>());

    @GetMapping(value = "/")
    public List<UserVO> getUserList(){
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/")
    public String postUser(@Valid @RequestBody UserSO user){
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        users.put(user.getId(),userVO);
        return "success";
    }

    @GetMapping("/{id}")
    public UserVO getUser(@PathVariable Long id){
        return users.get(id);
    }

    @GetMapping("/name/{name}")
    public UserVO getUserByName(@PathVariable String name){
        UserVO userVO = new UserVO();
        User user = userService.selectUserByName(name);
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }

}
