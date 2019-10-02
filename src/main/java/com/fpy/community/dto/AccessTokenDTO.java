package com.fpy.community.dto;
import lombok.Data;

@Data //帮助我们生成get、set、toString、hashCode、equals方法
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
