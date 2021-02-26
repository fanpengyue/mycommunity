//package com.fpy.community.job;
//
//import com.fpy.community.service.EmailService;
//import com.fpy.community.utils.ShellCommandUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Slf4j
////@Component
//public class MailJob {
//
//    @Value("${receive.email}")
//    private String receiveEmail;
//
//    @Autowired
//    private EmailService emailService;
//
//    /**
//     * 每隔两天的下午4点
//     */
//    @Scheduled(cron = "0 0 16 */2 * ?")
//    public void myTask() throws Exception{
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String time = simpleDateFormat.format(new Date());
//
//        /**
//        mysqldump -h localhost -uroot -pFANpeng1998@ community > /home/dump/community.sql
//        String[] commands = new String[]{"/bin/bash","-c" ,"cd /usr/installed/java_platform/updateFile/ && mkrom -x " + "iUTDR_V6.1.2.1.d001_b88_3481d5f9_SO_20200522_16h04m10s.el7.x86_64.rom"};
//        String shellRes = ShellCommandUtil.exec("commands",commands);
//         */
//
//        // 备份数据
//        String[] commands = new String[]{"/bin/bash","-c","mysqldump -h localhost -uroot -pFANpeng1998@ community > /home/dump/community.sql"};
//        ShellCommandUtil.exec("commands",commands);
//
//        Thread.sleep(1000 * 120);
//        // 发送邮件
//        emailService.sendWithDump(receiveEmail,"数据备份",time + " mysql数据库数据备份","/home/dump/community.sql");
//
//        Thread.sleep(1000 * 120);
//
//        //删除文件
//        String[] commands1 = new String[]{"/bin/bash","-c","cd /home/dump && rm -rf community.sql"};
//        ShellCommandUtil.exec("commands",commands1);
//
//    }
//}
