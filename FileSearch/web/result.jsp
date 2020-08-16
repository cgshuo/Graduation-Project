        <%--
  Created by IntelliJ IDEA.
  User: caoguangshuo
  Date: 2019/4/10
  Time: 4:51 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java"
         pageEncoding="UTF-8" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="lucene.file.search.service.RegexHtml" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="lucene.file.search.model.FileModel" %>
<%@ page import="java.util.Iterator" %>
<%
    String path = request.getContextPath(); //获取工程根目录
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String regEx_html = "<[^>]+>";
    Pattern r = Pattern.compile(regEx_html);
    ArrayList<String> hitsList = (ArrayList<String>) request.getAttribute("hitsList");
    String queryback = (String) request.getAttribute("queryback");

%>

        <!DOCTYPE html>
        <html>
<head>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keywords" content="keyword1, keyword2, keyword3">
    <meta http-equiv="description" content="This is my page">
    <meta http-equiv="Content-Type" content="text/html"; charset="UTF-8">
    <base href="<%=basePath%>">
    <title>搜索结果</title>
    <link type="text/css" rel="stylesheet" href="css/result.css">
</head>
<body>
    <div class="searchbox">
        <div class="logo">
            <a href="index.jsp" img alt="搜索" src="images/logo.png"></a>
        </div>
        <div class="searchform">
            <form action="SearchFile" method="get">
                <input type="text" name="query" value="<%=queryback%>">
                <input type="submit" value="Search">
            </form>
        </div>
    </div>
    <div class="result">
        <h4>
            共搜到<span style="color: red; font-weight: bold;"><%=hitsList.size()%></span>条结果
        </h4>
        <%
            if (hitsList.size() > 0) {
                Iterator<String> iter = hitsList.iterator();
                String fm;
                while (iter.hasNext()) {
                    fm = iter.next();
        %>
        <div class="item">
            <div class="itemtop">
            </div>
             <div class="itembuttom">
                 <p><%=fm%></p>
             </div>
                 <hr class="itemline">
        </div>
        <%
            }
            }
        %>

    </div>

</body>
</html>
