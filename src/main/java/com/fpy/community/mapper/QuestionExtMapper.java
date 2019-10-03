package com.fpy.community.mapper;

import com.fpy.community.model.Question;


public interface QuestionExtMapper {

    //实现增加评论数的功能
   int incrView(Question record);
}