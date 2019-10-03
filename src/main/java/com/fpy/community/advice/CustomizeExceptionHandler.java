package com.fpy.community.advice;

import com.alibaba.fastjson.JSON;
import com.fpy.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@ControllerAdvice //默认扫描所有
public class CustomizeExceptionHandler {
    @ExceptionHandler(Exception.class)
    ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if(e instanceof CustomizeException){ //如果是我们自定义的异常，即需要展示我们自己的信息
            model.addAttribute("message",e.getMessage());
        }else{
            model.addAttribute("message","服务出错了，请稍后再试！！！");
        }
            return new ModelAndView("error");
    }
}
