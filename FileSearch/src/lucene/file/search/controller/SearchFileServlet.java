package lucene.file.search.controller;

import lucene.file.search.model.FileModel;
//import lucene.file.search.service.CNAndENMap;
import lucene.file.search.service.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.document.Document;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:caoguangshuo
 * @date:2019/4/10
 * @descripstion:
 * getTopDOC() 搜索，返回结果为FileModel类型的list，传入参数为查询关键词、索引路径、返回文档的条数
 **/
public class SearchFileServlet extends HttpServlet {


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException ,IOException {
       // System.out.println("SearchFilesServlet");
       // String indexpathStr = request.getServletContext().getRealPath("/Volumes/MAC/GraIndexdir");//索引路径
        String indexpathStr = "/Volumes/MAC/GraIndexdir";
        String query = request.getParameter("query"); //接收查询字符
        // query = new String(query.getBytes("ISO-8859-1"), "UTF-8"); //编码格式转换
        if(query.equals("") || query == null || query == "NullByCGS"){
            System.out.println("参数错误");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }else {
            ArrayList<String> hitsList  = new ArrayList<>();//= getTopDoc(query, indexpathStr,100,analyzer);
            if(isChineseOrEnglish(query) == "isChinese"){
                IKAnalyzer6x analyzerCN = new IKAnalyzer6x();
                hitsList = getTopDoc(query, indexpathStr,100,analyzerCN);
                //String filePath = "/Volumes/MAC/毕业设计资料/英语单词对应中文拆分版.txt";
                Map<String, String> map = new HashMap<>();
                ServletContext application = this.getServletContext();
                map = (Map<String, String>) application.getAttribute("dictionary");//<CN,EN>
                query=findWord(query,map);
            }
            Analyzer analyzer = new StandardAnalyzer();
            hitsList.addAll(getTopDoc(query, indexpathStr,100,analyzer));
            System.out.println("共搜到：" + hitsList.size() + "条数据！");
            request.setAttribute("hitsList", hitsList);
            request.setAttribute("queryback", query);
            request.getRequestDispatcher("result.jsp").forward(request, response);
        }

    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        doGet(request, response);
    }

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
    public static ArrayList<String> getTopDoc(String key, String indexpathStr, int N, Analyzer analyzer){
        ArrayList<String> hitsList = new ArrayList<String>();
        String[] fields = {"content"};
        Path indexPath = Paths.get(indexpathStr);
        Directory dir;
        try {
            dir = FSDirectory.open(indexPath);
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher indexSearcher = new IndexSearcher(reader);

            MultiFieldQueryParser parser2 = new MultiFieldQueryParser(fields, analyzer);

            //查询字符串
            Query query = parser2.parse(key);
            TopDocs topDocs = indexSearcher.search(query, N);

            //定制高亮标签
            SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<span style = \"color:red;\">", "</span>");
            QueryScorer scoreContent = new QueryScorer(query, fields[0]);
            Highlighter hlqContent = new Highlighter(fors, scoreContent);
            TopDocs hits = indexSearcher.search(query, 100);
            for(ScoreDoc sd : topDocs.scoreDocs){
                Document doc = indexSearcher.doc(sd.doc);
                //  String title = doc.get("title");
                String content = doc.get("content");
                TokenStream tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), sd.doc, fields[0], new StandardAnalyzer());
                tokenStream = TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(), sd.doc, fields[0], new StandardAnalyzer());
                Fragmenter fragment = new SimpleSpanFragmenter(scoreContent);
                hlqContent.setTextFragmenter(fragment);
                String hl_content = hlqContent.getBestFragment(tokenStream, content);//获取高亮的片段，可以对其数量进行限制
                hitsList.add(hl_content != null ? hl_content : content);
            }
            dir.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return hitsList;
    }

    }
