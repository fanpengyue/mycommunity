package com.fpy.community.controller;

import com.fpy.community.dto.PaginationDTO;
import com.fpy.community.mapper.UserMapper;
import com.fpy.community.model.User;
import com.fpy.community.service.NotificationService;
import com.fpy.community.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {

    @Resource
    private UserMapper userMapper;

    @Resource
    QuestionService questionService;

    @Resource
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable(name = "action") String action, Model model,
                          HttpServletRequest request,
                          @RequestParam(name = "page",defaultValue = "1")Integer page,
                          @RequestParam(name = "size",defaultValue = "5")Integer size) {
        //查询出当前登录的用户信息
        User user = (User)request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }
        if ("questions".equals(action)) {
            model.addAttribute("section", "questions");
            model.addAttribute("sectionName", "我的提问");
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            model.addAttribute("pagination",paginationDTO);
        } else if ("replies".equals(action)) {
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            model.addAttribute("section", "replies");
            model.addAttribute("pagination", paginationDTO);
            model.addAttribute("sectionName", "我的回复");
        }

        return "profile";
    }
}
