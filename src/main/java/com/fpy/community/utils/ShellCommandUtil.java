package com.fpy.community.utils;

import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ShellCommandUtil {
    /** 判断OS 类型确定使用哪种编码,windows 使用GBK,Linux 使用UTF-8 */
    static String forName = System.getProperties().getProperty("os.name").toLowerCase().contains("windows") ?"GBK":"UTF-8";
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ShellCommandUtil.class);
    public static String exec(String command)  {
        logger.info("解压命令====>"+command);
        /*try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        String returnString = "";
        Process pro = null;
        Runtime runTime = Runtime.getRuntime();
        if (runTime == null) {
            System.err.println("Create runtime false!");
        }
        try {
            pro = runTime.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pro.getInputStream(), Charset.forName(forName)));
            PrintWriter output = new PrintWriter(new OutputStreamWriter(pro.getOutputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                returnString = returnString + line + "\n";
            }
            input.close();
            output.close();
            pro.destroy();
        } catch (IOException ex) {
            Logger.getLogger(ShellCommandUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnString;
    }

    /**
     *
     * @param message
     * @param args
     * @return
     * @throws Exception
     */
    public static String exec(String message, String[] args)  {

        String result = "";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(args);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        String errorLine = null;
        String tempLine = null;
        try {
            while (( tempLine= errorReader.readLine()) != null) {
                errorLine +=tempLine;
            }
            Logger.getLogger(ShellCommandUtil.class.getName()).log(Level.WARNING,"--errorReader--:"+errorLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            errorReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader infoReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = null;
        try {
            while ((line = infoReader.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            infoReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(forName);
        System.out.println(exec("ping www.baidu.com"));
        String[] commands = new String[]{"/bin/bash","-c" ,"cd /usr/installed/java_platform/updateFile/ && mkrom -x " + "iUTDR_V6.1.2.1.d001_b88_3481d5f9_SO_20200522_16h04m10s.el7.x86_64.rom"};
        String shellRes = ShellCommandUtil.exec("commands",commands);

        String[] commands1 = new String[]{"/bin/bash","-c" ,"ifconfig -s | awk '{if($1!~/Iface/ )print $1}' | awk '{if($1!~/lo/ )print $1}'"};
        String shellRes1 = ShellCommandUtil.exec("commands",commands);

        System.out.println(shellRes1);
    }
}
