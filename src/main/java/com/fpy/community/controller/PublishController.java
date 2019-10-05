package com.fpy.community.controller;

import com.fpy.community.cache.TagCache;
import com.fpy.community.dto.QuestionDTO;
import com.fpy.community.model.Question;
import com.fpy.community.model.User;
import com.fpy.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Resource
    private QuestionService questionService;


    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @GetMapping("/publish/{id}")
    public String edit(@PathVariable(name = "id")Long id,
                       Model model){
        QuestionDTO question = questionService.getDetailById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        model.addAttribute("tags",TagCache.get());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(@RequestParam("title")String title,
                            @RequestParam("description")String description,
                            @RequestParam("tag")String tag,
                            @RequestParam("id")Long id,
                            HttpServletRequest request,
                            Model model){
        //出现问题的时候，可以保存到用户的输入内容，并展示到页面上，避免二次输入
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",TagCache.get());
        //标题不可以为空
        if(title == null || title == ""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        //问题描述不可以为空
        if(description == null || description == ""){
            model.addAttribute("error","问题不能为空");
            return "publish";
        }
        //标签不可以为空
        if(tag == null || tag == ""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        //判断用户输入的标签是否包含在我们的标签库当中
        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }
        //查询出当前登录的用户信息
        User user = (User)request.getSession().getAttribute("user");

        //判断用户是否登录了
        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        //保存用户输入的问题，并插入数据库
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setId(id);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }
}
