package com.david.springboot.bean;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class User {

    @NotNull(message = "id不能为空")
    private Long id;

    @NotEmpty(message = "name不能为空")
    private String name;

    @Email
    @NotEmpty(message = "email不能为空")
    private String email;
}
