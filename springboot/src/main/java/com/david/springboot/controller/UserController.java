package com.david.springboot.controller;

import com.david.springboot.bean.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping(value = "/users")
public class UserController {
    @Value("${com.david.name}")
    private String name;

    /**
     * 创建线程安全的Map,存放用户数据
     */
    private static Map<Long, User> users = Collections.synchronizedMap(new HashMap<>());

    @GetMapping(value = "/")
    public List<User> getUserList(){
        return new ArrayList<>(users.values());
    }

    @PostMapping(value = "/")
    public String postUser(@Valid @RequestBody User user){
        users.put(user.getId(),user);
        return "success";
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        return users.get(id);
    }

}
