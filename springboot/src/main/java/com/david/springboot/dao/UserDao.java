package com.david.springboot.dao;

import com.david.springboot.bean.model.User;
import org.apache.ibatis.annotations.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM user WHERE name = #{name}")
    User findUserByName(@Param("name") String name);

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Insert("INSERT INTO user(name,age,money) VALUES (#{name}, #{age}, #{money})")
    void insertUser(@Param("name") String name,@Param("age") Integer age,@Param("money") double money);

    @Update("UPDATE user SET money =#{money} WHERE name = #{name}")
    void updateUser(@Param("money") double money,@Param("name") String name);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void deleteUser(@Param("id") Long id);
}
