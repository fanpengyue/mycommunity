package com.fpy.community.mapper;


import com.fpy.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment comment);
}