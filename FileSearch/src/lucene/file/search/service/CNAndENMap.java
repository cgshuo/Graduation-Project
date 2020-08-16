//package lucene.file.search.service;
//
//import java.io.*;
//import java.util.HashMap;
//import java.util.Map;
//
//
///**
// * @author:caoguangshuo
// * @date:2019/5/5
// * @descripstion:
// **/
//public class CNAndENMap {
//    public static Map<String, String> map = new HashMap<>();
//
//
//    public static Map<String, String> readTxtFile(String filePath) {
//        Map<String, String> map = new HashMap<>();
//        String encoding = "UTF-8";
//        File file = new File(filePath);
//        if (file.isFile() && file.exists()) {
//            try {
//                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
//                BufferedReader bufferedReader = new BufferedReader(reader);
//                String lineTXT = null;
//                while ((lineTXT = bufferedReader.readLine()) != null) {
//                    System.out.println(lineTXT);
//                    String[] CHAndEN = lineTXT.split("\\s+");
//                    map.put(CHAndEN[1], CHAndEN[0]);
//                }
//                reader.close();
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return map;
//    }
//    public static void main(String[]args){
//        String filePath = "/Volumes/MAC/毕业设计资料/英语单词对应中文拆分版.txt";
//        map = readTxtFile(filePath);
//    }
//
//}
