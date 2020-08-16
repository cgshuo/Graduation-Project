import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static String isChineseOrEnglish(String charaString){
        boolean result =false;
        //英文
        result=charaString.matches("^[a-zA-Z]*");
        if(result){
            return "isEnglish";
        }
        //中文
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(charaString);
        result=m.find();
        if(result){
            return "isChinese";
        }else{
            return "isChineseAndEnglish";//中英结合
        }
    }

    public static String checkChineseOrEnglish(String word){
        byte []bytes = word.getBytes();
        if(bytes.length==word.length()){
            return "isEnglish";
        }else{
            return "isChinese";
        }
    }

    public static Map<String, String> readTxtFile(String filePath){
        Map<String, String> map =new HashMap<>();
        String encoding = "UTF-8";
        File file = new File(filePath);
        if(file.isFile() && file.exists()){
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTXT = null;
                while((lineTXT = bufferedReader.readLine()) != null){
                    System.out.println(lineTXT);
                    String[] CHAndEN = lineTXT.split("\\s+");
                    map.put(CHAndEN[1],CHAndEN[0]);
                }
                reader.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return map;
    }


    public static String findWord(String words, Map<String, String> map){
        if(isChineseOrEnglish(words) == "isEnglish")
            return words;
        else {
            if (map.containsKey(words)){
                System.out.println("*****"+words);
                return map.get(words);

            }
            else{
                System.out.println("没有找到对应英文");
                return "NullByCGS";
            }

        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "/Volumes/MAC/毕业设计资料/英语单词对应中文拆分版.txt";
        Map<String, String> map = new HashMap<>();
        map = readTxtFile(filePath);


        while (scanner.hasNext()) {
            //利用nextXXX()方法输出内容
            String str = scanner.next();
            System.out.println(str);
            System.out.println("***********************************");
            System.out.println("查询结果：");
            System.out.println(findWord(str, map));
            System.out.println("***********************************");



        }
//        System.out.println(Main.isChineseOrEnglish("明白领"));
//
//        System.out.println(Main.checkChineseOrEnglish("what happend"));
//        System.out.println(Main.checkChineseOrEnglish("47"));
    }
}