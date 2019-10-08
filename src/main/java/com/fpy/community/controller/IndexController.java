package com.fpy.community.controller;

import com.fpy.community.dto.PaginationDTO;
import com.fpy.community.mapper.UserMapper;
import com.fpy.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
                        @RequestParam(name = "size",defaultValue = "5")Integer size,
                        @RequestParam(name = "search",required = false)String search){

        if(search ==""||search == null){
            search=null;
        }
        PaginationDTO paginationDTO = questionService.list(search,page,size);
        model.addAttribute("pagination",paginationDTO);
        model.addAttribute("search", search);
        return "index";
    }

}
