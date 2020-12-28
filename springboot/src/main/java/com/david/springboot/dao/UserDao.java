package com.david.springboot.dao;

import com.david.springboot.bean.model.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.mapstruct.Mapper;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM user WHERE name = #{name}")
    User findUserByName(@Param("name") String name);

}
