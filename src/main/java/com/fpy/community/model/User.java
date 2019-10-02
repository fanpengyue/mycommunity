package com.fpy.community.model;
import lombok.Data;

@Data //帮助我们生成get、set、toString、hashCode、equals方法
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
