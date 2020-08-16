package lucene.file.search.controller;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.rmi.server.ServerCloneException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:caoguangshuo
 * @date:2019/5/5
 * @descripstion:
 **/
public class TranslateServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        ServletContext servletContext = this.getServletContext();
        String filePath = "/Volumes/MAC/毕业设计资料/英语单词对应中文拆分版.txt";
        Map<String, String> map = new HashMap<>();
        map = readTxtFile(filePath);
        servletContext.setAttribute("dictionary", map);
        request.getRequestDispatcher("index2.jsp").forward(request, response);
    }


    public static Map<String, String> readTxtFile(String filePath) {
        Map<String, String> map = new HashMap<>();
        String encoding = "UTF-8";
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String lineTXT = null;
                while ((lineTXT = bufferedReader.readLine()) != null) {
                    String[] CHAndEN = lineTXT.split("\\s+");
                    map.put(CHAndEN[1], CHAndEN[0]);
                }
                System.out.println("字典提取完成");
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



}
