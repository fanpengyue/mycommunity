package com.fpy.community.controller;

import com.fpy.community.dto.CommentDTO;
import com.fpy.community.dto.QuestionDTO;
import com.fpy.community.enums.CommentTypeEnum;
import com.fpy.community.service.CommentService;
import com.fpy.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class QuestionController {
    @Resource
    private QuestionService questionService;

    @Resource
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id,
                           Model model){
        QuestionDTO questionDTO = questionService.getDetailById(id);

        List<QuestionDTO> relatedQuestion = questionService.selectRelated(questionDTO);

        //查询出改问题的回复列表
        List<CommentDTO> comments = commentService.listByTargetId(id,CommentTypeEnum.QUESTION);

        questionService.incrView(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",comments);
        model.addAttribute("relatedQuestions",relatedQuestion);
        return "question";
    }
}
