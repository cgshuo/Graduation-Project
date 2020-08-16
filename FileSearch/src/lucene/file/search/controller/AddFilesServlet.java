package lucene.file.search.controller;

import lucene.file.search.model.FileModel;
import lucene.file.search.service.IKAnalyzer6x;
import lucene.file.search.service.ToSentence;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static lucene.file.search.service.ToSentence.ArticleToSentences;

/**
 * @author:caoguangshuo
 * @date:2019/5/8
 * @descripstion:
 **/


public class AddFilesServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
        //String savePath = this.getServletContext().getRealPath("/web/upload");
        String savePath = "/Volumes/MAC/AddFile";
        File file = new File(savePath);
        if(!file.exists()&&!file.isDirectory()){
            System.out.println("目录或文件不存在！");
            file.mkdir();
        }
//        File TrxFiles[] = file.listFiles();
//        for(File curFile:TrxFiles ){
//            curFile.delete();
//        }
        String language = "english";
        try {
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload fileUpload = new ServletFileUpload(diskFileItemFactory);
            //解决上传文件名的中文乱码
            fileUpload.setHeaderEncoding("UTF-8");
            //3、判断提交上来的数据是否是上传表单的数据
            if(!fileUpload.isMultipartContent(request)){
                //按照传统方式获取数据
                return;
            }
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = fileUpload.parseRequest(request);
            for (FileItem item : list) {
                //如果fileitem中封装的是普通输入项的数据
                if(item.isFormField()){
                    String name = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    String value1 = new String(name.getBytes("iso8859-1"),"UTF-8");
                    System.out.println(name+"  "+value);
                    System.out.println(name+"  "+value1);
                }else{
                    //如果fileitem中封装的是上传文件，得到上传的文件名称，
                    String fileName = item.getName();
                    System.out.println(fileName);
                    if(fileName==null||fileName.trim().equals("")){
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    fileName = fileName.substring(fileName.lastIndexOf(File.separator)+1);
                    //获取item中的上传文件的输入流
                    if(!isEnglish(fileName)){
                        language = "chinese";
                    }
                    InputStream is = item.getInputStream();
                    //创建一个文件输出流
                    FileOutputStream fos = new FileOutputStream(savePath+File.separator+fileName);
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int length = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while((length = is.read(buffer))>0){
                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                        fos.write(buffer, 0, length);
                    }
                    //关闭输入流
                    is.close();
                    //关闭输出流
                    fos.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();

                }
            }
        } catch (FileUploadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //System.out.println("***********1");
        createIndex(language);
        //System.out.println("***********2");
        response.sendRedirect("start.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    public static List<FileModel> extractFile() throws IOException{
        ArrayList<FileModel> list = new ArrayList<FileModel>();
        File fileDir = new File("/Volumes/MAC/AddFile");
        File[] allFiles = fileDir.listFiles();
        for(File f : allFiles){
            FileModel sf = new FileModel(f.getName(), ParserExtraction(f));
            list.add(sf);
        }
        return list;
    }
    public static String ParserExtraction(File file) {
        String fileContent = ""; //接收文档内容
        BodyContentHandler handler = new BodyContentHandler(10*1024*1024);
        Parser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            ParseContext context = new ParseContext();
            parser.parse(inputStream, handler, metadata, context);
            fileContent = handler.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }

    public static void createIndex(String language) throws IOException{
        Analyzer analyzer;
        if(language == "chinese"){
            //IK分词器对象
             analyzer = new IKAnalyzer6x();
        }else{
            //StandardAnalyzer
             analyzer = new StandardAnalyzer();
        }
        IndexWriterConfig icw = new IndexWriterConfig(analyzer);
        icw.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory dir = null;
        IndexWriter indexWriter = null;
        //Path indexPath = Paths.get("web/indexdir");
        Path indexPath = Paths.get("/Volumes/MAC/GraIndexdir");
        FieldType fileType = new FieldType();
        fileType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fileType.setStored(true);
        fileType.setTokenized(true);
        fileType.setStoreTermVectors(true);
        fileType.setStoreTermVectorPositions(true);
        fileType.setStoreTermVectorOffsets(true);

        Date start = new Date(); // start time
        if(!Files.isReadable(indexPath)){
            System.out.println(indexPath.toAbsolutePath() + "不存在或者不可读，请检查！");
            System.exit(1);
        }
        dir = FSDirectory.open(indexPath);
        indexWriter = new IndexWriter(dir, icw);
        ArrayList<FileModel> fileList = (ArrayList<FileModel>)extractFile();
        //遍历fileList，建立索引
        for(FileModel f : fileList){
            ToSentence s = new ToSentence();
            System.out.println(s.splitfuhao(f.getContent()));
            List<String> sl = ArticleToSentences(s.splitfuhao(f.getContent()));
            if(sl.isEmpty()){
                System.out.println("没有识别到句子");
            }
            for (String row : sl) {
                // sentenceList.add(row);
                Document doc = new Document();
                // doc.add(new Field("title", f.getTitle(), fileType));
                doc.add(new Field("content", row, fileType));
                indexWriter.addDocument(doc);
            }

        }
        indexWriter.commit();
        indexWriter.close();
        dir.close();
        Date end = new Date();

        System.out.println("索引文档完成，共耗时：" + (end.getTime() - start.getTime()) + "毫秒.");
    }
    public boolean isEnglish(String sentence){
        char[] ch =  sentence.toCharArray();
        for (char c : ch) {
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
            if(ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                    ||ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                    ||ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                    ||ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  // GENERAL_PUNCTUATION 判断中文的“号
                    ||ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION     // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
                    ||ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS    // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
                    )
                return false;
        }
        return true;
    }


}