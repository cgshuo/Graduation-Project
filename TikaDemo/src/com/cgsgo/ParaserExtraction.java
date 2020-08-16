package com.cgsgo;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author:caoguangshuo
 * @date:2019/4/8
 * @descripstion: Extrated by Paraser automagically
 **/
public class ParaserExtraction {
    public static void main(String[] args) throws IOException, TikaException, SAXException {
        File fileDir = new File("files");
        if (!fileDir.exists()){
            System.out.println("文件夹不存在！");
            System.exit(0);
        }
        File[] fileArr = fileDir.listFiles();
        BodyContentHandler handler = new BodyContentHandler();
        Metadata metadata = new Metadata();
        FileInputStream inputStream = null;
        Parser parser = new AutoDetectParser();

        ParseContext context = new ParseContext();
        for (File f : fileArr){
            inputStream = new FileInputStream(f);
            parser.parse(inputStream, handler, metadata, context);
            System.out.println(f.getName() + ":\n" + handler.toString());
        }

    }
}

