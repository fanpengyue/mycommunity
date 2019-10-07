package com.fpy.community.controller;

import com.fpy.community.dto.FileDTO;
import com.fpy.community.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;

@Controller
public class FileController {
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDTO upload(HttpServletRequest request) {
        //获取到用户的id，用于区分开文件夹
        User user = (User) request.getSession().getAttribute("user");
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");

        //图片的名称
        String time = String.valueOf(System.currentTimeMillis());
        String fileName = time.substring(2, 8) + file.getOriginalFilename();
        String path = "D:\\jar\\" + user.getId();
        //判断目录是否存在
        File dir = new File(path, fileName);

        if (!dir.exists()) { //不存在要创建目录
            dir.mkdirs();
        }
        // MultipartFile自带的解析方法
        try {
            file.transferTo(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //这里我在拼接出我图片要显示的url
        String imageurl = "http://localhost:8089/images/" +user.getId()+"/"+ fileName;

        FileDTO fileDTO = new FileDTO();
        fileDTO.setSuccess(1);
        fileDTO.setUrl(imageurl);

        return fileDTO;
//        -------------------------------------------------------------------------------------------------------
//        User user = (User) request.getSession().getAttribute("user");
//        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
//        MultipartFile file = multipartHttpServletRequest.getFile("editormd-image-file");
//        //图片的路径
//        String path = request.getSession().getServletContext().getRealPath("/images");
//        //图片的名称
//        String time = String.valueOf(System.currentTimeMillis());
//        String fileName = time.substring(2, 8) + file.getOriginalFilename();
//
//        //判断目录是否存在
//        File dir = new File(path, fileName);
////        System.out.println("判断目录是否存在："+dir.exists());
//        if (!dir.exists()) { //不存在要创建目录
//            dir.mkdirs();
//        }
//        // MultipartFile自带的解析方法
//        try {
//            file.transferTo(dir);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //这里我在拼接出我图片要显示的url
//        String imageurl = "http://localhost:8089/images/" +user.getId()+""+ fileName;
//
//        FileDTO fileDTO = new FileDTO();
//        fileDTO.setSuccess(1);
//        fileDTO.setUrl(imageurl);
    }
}
