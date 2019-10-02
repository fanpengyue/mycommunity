package com.fpy.community.model;

import lombok.Data;

@Data //帮助我们生成get、set、toString、hashCode、equals方法
public class Question {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
