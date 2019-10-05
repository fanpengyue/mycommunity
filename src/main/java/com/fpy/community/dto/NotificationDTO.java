package com.fpy.community.dto;

import lombok.Data;

/**
 * Created by codedrinker on 2019/6/14.
 */
@Data
public class NotificationDTO {
    private Long id; //展示本条通知的id
    private Long gmtCreate; //展示创建的时间
    private Integer status;
    private Long notifier;
    private String notifierName;  //通知人的姓名
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}
