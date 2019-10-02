package com.fpy.community.controller;

import com.fpy.community.dto.QuestionDTO;
import com.fpy.community.mapper.QuestionMapper;
import com.fpy.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.Resource;

@Controller
public class QuestionController {
    @Resource
    private QuestionService questionService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id,
                           Model model){
        QuestionDTO questionDTO = questionService.getDetailById(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
