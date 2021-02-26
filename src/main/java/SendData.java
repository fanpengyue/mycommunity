import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SendData {

    public static void main(String[] args) {
        System.out.println("输入文件路径和名称,例如(/home/detection_results.json):");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.nextLine();

        List<String> content = readFileContent(path);
        System.out.println("共"+content.size()+"行");
        System.out.println("输入es地址，例如(10.10.10.11:9201):");
        String address = scanner.nextLine();

        System.out.println("输入索引,例如(detection_results):");
        String index = scanner.nextLine();

        System.out.println("输入索引type,例如(detection_results):");
        String type = scanner.nextLine();

        int i=0;
        for(String data : content) {
            String id = data.substring(data.indexOf("\"_id\":\"")+7,data.indexOf(",\"_score\"")-1);
//            String source = data.substring(data.indexOf("\"_source\":{") + 10,data.lastIndexOf(",\"sort\""));
            String source = data.substring(data.indexOf("\"_source\":{") + 10,data.length()-1);
            try{
                String curl = " curl -XPOST http://" + address + "/" + index + "/" + type + "/" + id + " -d '" + source + "'";
                System.out.println("插入指令：" + curl);
                String[] upgradeCommands = new String[]{"/bin/bash", "-c", curl};
                String upgradeRes = exec("commands", upgradeCommands);
            }catch (Exception e){
                e.printStackTrace();
            }
            i++;
        }
        System.out.println("完成"+i+"行");


    }



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

    public static String get(String path) {
        StringBuffer stringBuffer = new StringBuffer();

        try {
            File file = new File(path);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            String s = (new String(stringBuffer)).replace(" ", "");
            return s;
        } catch (Exception var6) {
            return "1";
        }
    }

    /**
     * 根据文件路径读取文件内容
     * @param filePath  文件路径
     * @return
     */
    public static List<String> readFileContent(String filePath) {
        BufferedReader reader = null;
        List<String> listContent = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filePath));
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(filePath),"GB2312"));
            String tempString = null;
            int line = 1;

            while ((tempString = reader.readLine()) != null) {
                listContent.add(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return listContent;
    }
}
