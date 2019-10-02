package com.fpy.community.controller;

import com.fpy.community.dto.PaginationDTO;
import com.fpy.community.dto.QuestionDTO;
import com.fpy.community.mapper.QuestionMapper;
import com.fpy.community.mapper.UserMapper;
import com.fpy.community.model.Question;
import com.fpy.community.model.User;
import com.fpy.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Resource
    private QuestionService questionService;
    @Resource
    private UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request,
                        Model model,
                        @RequestParam(name = "page",defaultValue = "1")Integer page,
                        @RequestParam(name = "size",defaultValue = "5")Integer size){
        PaginationDTO paginationDTO = questionService.list(page,size);
        model.addAttribute("pagination",paginationDTO);
        return "index";
    }

}
