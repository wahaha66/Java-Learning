package com.david.springboot.dao.p;

import com.david.springboot.bean.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface UserMapperPrimary {
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

    @Select("SELECT * FROM USER WHERE NAME = #{name}")
    User findByName(@Param("name") String name);

    @Insert("INSERT INTO USER(NAME, AGE) VALUES(#{name}, #{age})")
    int insert(@Param("name") String name, @Param("age") Integer age);

    @Delete("DELETE FROM USER")
    int deleteAll();
}
