//package com.fpy.community.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import javax.mail.internet.MimeMessage;
//import java.io.File;
//
//@Service
//public class EmailService {
//    @Autowired
//    JavaMailSender jms;
//
////    @Value("${receive.email}")
////    private String receiveEmail;
//
//    @Value("${spring.mail.username}")
//    private String senderEmail;
//
//    /**
//     *发送文本消息，不带附件
//     */
//    public void send(String receiveEmail,String title,String content){
//        //建立邮件消息
//        SimpleMailMessage mainMessage = new SimpleMailMessage();
//        //发送者
//        mainMessage.setFrom(senderEmail);
//        //接收者
//        mainMessage.setTo(receiveEmail);
//        //发送的标题
//        mainMessage.setSubject(title);
//        //发送的内容
//        mainMessage.setText(content);
//        jms.send(mainMessage);
//    }
//
//    /**
//     * 发送文件消息，带附件
//     */
//    public void sendWithDump(String receiveEmail,String title,String content,String ...fileArray ){
////        String [] fileArray={"/home/data/dump/mysql.dump"};
////        String [] fileArray={"C:\\Users\\48472\\Desktop\\沙箱.csv"};
//
//        MimeMessage message=jms.createMimeMessage();
//        try {
//            MimeMessageHelper helper=new MimeMessageHelper(message,true);
//            helper.setFrom(senderEmail);
//            helper.setTo(receiveEmail);
//            helper.setSubject(title);
//            helper.setText(content);
//            //验证文件数据是否为空
//            if(null != fileArray){
//                FileSystemResource file=null;
//                for (int i = 0; i < fileArray.length; i++) {
//                    //添加附件
//                    file=new FileSystemResource(fileArray[i]);
//                    helper.addAttachment(fileArray[i].substring(fileArray[i].lastIndexOf(File.separator)), file);
//                }
//            }
//            jms.send(message);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//}
