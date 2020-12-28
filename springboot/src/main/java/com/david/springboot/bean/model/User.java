package com.david.springboot.bean.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class User {
    private int id;
    private String name;
    private int age;
    private double money;
}
