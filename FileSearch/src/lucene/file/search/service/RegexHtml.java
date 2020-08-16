package lucene.file.search.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author:caoguangshuo
 * @date:2019/4/10
 * @descripstion: 去除html标签的正则表达式类
 **/
public class RegexHtml {
    public String delHtmlTag(String line){
        String regEx_html = "<[^>]+>";
        Pattern r = Pattern.compile(regEx_html);
        Matcher m = r.matcher(line);
        line = m.replaceAll("");
        return line;
    }
}
