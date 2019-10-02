package com.fpy.community.dto;

import lombok.Data;

@Data //帮助我们生成get、set、toString、hashCode、equals方法
public class GithubUser {
    private String name; //姓名
    private Long id; //id
    private String bio; //描述
    private String avatarUrl;//头像url
}
