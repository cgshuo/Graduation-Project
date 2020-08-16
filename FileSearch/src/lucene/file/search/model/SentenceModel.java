package lucene.file.search.model;

/**
 * @author:caoguangshuo
 * @date:2019/5/3
 * @descripstion:
 **/
public class SentenceModel {

    private String content; // 文件内容

    public String getContent(){
        return content;
    }
    public void setContent(String content){
        this.content = content;
    }

   public SentenceModel(){}
   public SentenceModel(String content){
        this.content = content;
   }

}

