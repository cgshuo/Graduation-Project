package lucene.file.search.model;

/**
 * @author:caoguangshuo
 * @date:2019/4/9
 * @descripstion: 实体类
 **/
public class FileModel {
    private String title; //文件标题
    private String content; // 文件内容

    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

    public FileModel(){}
    public FileModel(String title, String content){
        this.title = title;
        this.content = content;

    }

}
