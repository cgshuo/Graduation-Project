package com.cgsgo;
/*
    Created by cgs
 */
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;


public class TikaParsePdf {

    public static void main(String[] args) throws IOException, SAXException, TikaException {

        String filepath = "files/B.pdf";
        File pdfFile = new File(filepath);
        BodyContentHandler handler = new BodyContentHandler();//create bodycontenthandler
        Metadata metadata = new Metadata();
        FileInputStream inputStream = new FileInputStream(pdfFile);
        //InputStream inputStream1 = TikaInputStream.get(pdfFile);
        ParseContext parseContext = new ParseContext();
        // 实例化PDFParaser 对象
        /*
        MS Office : OOXMLParaser parser = new OOXMLParser();
        TXT: TXTParaser
        HTML: Htmlparser
        XML: XMLParser
        class: ClassParser
         */
        PDFParser parser = new PDFParser();
        // 调用parse()方法解析文件
        parser.parse(inputStream, handler, metadata, parseContext);
        //遍历元数据内容
        System.out.println("文件属性信息：");
        for(String name: metadata.names()){
            System.out.println(name + ":" + metadata.get(name));
        }
        System.out.println("pdf文件中的内容");


        PrintStream ps = new PrintStream("files/B.txt");
        System.setOut(ps);

        System.out.println(handler.toString());

    }
}
