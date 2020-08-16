<%--
  Created by IntelliJ IDEA.
  User: caoguangshuo
  Date: 2019/5/8
  Time: 2:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>

    <title>SearchInterface</title>
    <style type="text/css">
        .head{  color:#fff;position:absolute;right:0;top:0;margin:19px 0 5px;padding:0 96px 0 0;}
        .mav{float: left;color: #333;font-weight: 700;line-height: 24px;margin-left: 20px;font-size: 13px;text-decoration:underline;}
        .head a:hover{color:#00c}
        .centers{ text-align: center; }
        .gen{position: absolute;right: 10px;width: 60px;height: 23px;float: left;color: #fff;background: #3385ff;line-height: 24px;font-size: 13px;text-align: center;border-bottom: 1px solid #38f;margin-left: 19px;text-decoration: none}
        .imgs{margin-top: 30px}
        .sone{border: 1px solid #b6b6b6;background: #fff;display: inline-block;vertical-align:top;width: 539px;height:34px;border-color: #b8b8b8 #ccc #ccc #b8b8b8;overflow: hidden;}
        span{margin: 0;padding: 0}
        input{border: 0;padding: 0;}
        .sn{height: 16px;width: 18px;}
        .sou{width: 279px;height: 22px;font: 16px/18px arial;line-height: 18px;margin: 6px 0 0 7px;padding: 0;outline: 0;}
        .btn{width: 100px; height: 36px; color: #fff; font-size: 15px; letter-spacing: 1px; background: #3385ff;}
        .tail{text-align: center;}
        .one{height: 191.5px;}
        p,p>a{color: #999; line-height: 22px; font: 12px arial;}
        #lh a{margin-right:12px;}
        i{width: 14px;height: 17px;display: inline-block;}
        .containter{height:100px;line-height:100px;background:#999;}
    </style>
</head>
<body>
<table border=0 style="width:100% ;height:60%">
    <tr><td align="center" valign="middle">
        <div class="search">
            <div class="centers">
                <div class="imgs">
                    <font size="27">UploadFiles</font>
                </div>
                <form action="/AddFiles" method="post" enctype="multipart/form-data">
			            <input type="file" name="files" class="sou" >
			            <input class="btn" type="submit" value="AddFile" >
                     <%--</span>--%>
                </form>
            </div>
        </div>
</table>
<div class="tail">
    <div class="thr">
        <p id="cp">
            ©2019 <a href="http://www.cgsgo.com">cgs </a>
        </p>
    </div>
</div>

</div>
</body>
</html>