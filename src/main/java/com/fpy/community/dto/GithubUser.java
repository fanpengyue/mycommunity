package com.fpy.community.dto;

public class GithubUser {
    private String name; //姓名
    private Long id; //id
    private String bio; //描述

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public String getBio() {
        return bio;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
