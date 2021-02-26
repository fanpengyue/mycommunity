//package com.fpy.community;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.io.FileSystemResource;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import javax.mail.internet.MimeMessage;
//import java.io.File;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class MailTest {
//
//    @Autowired
//    JavaMailSender jms;
//
//    @Value("${receive.email}")
//    private String receiveEmail;
//
//    @Value("${spring.mail.username}")
//    private String senderEmail;
//
//    /**
//     *发送文本消息，不带附件
//     */
//    @Test
//    public void send(){
//        //建立邮件消息
//        SimpleMailMessage mainMessage = new SimpleMailMessage();
//        //发送者
//        mainMessage.setFrom(senderEmail);
//        //接收者
//        mainMessage.setTo(receiveEmail);
//        //发送的标题
//        mainMessage.setSubject("数据备份");
//        //发送的内容
//        mainMessage.setText("数据备份测试");
//        jms.send(mainMessage);
//    }
//
//    /**
//     * 发送文件消息，带附件
//     */
//    @Test
//    public void sendWithDump(){
////        String [] fileArray={"/home/data/dump/mysql.dump"};
//        String [] fileArray={"C:\\Users\\48472\\Desktop\\沙箱.csv"};
//
//        MimeMessage message=jms.createMimeMessage();
//        try {
//            MimeMessageHelper helper=new MimeMessageHelper(message,true);
//            helper.setFrom(senderEmail);
//            helper.setTo(receiveEmail);
//            helper.setSubject("附件备份数据测试");
//            helper.setText("测试数据");
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
//
//}
