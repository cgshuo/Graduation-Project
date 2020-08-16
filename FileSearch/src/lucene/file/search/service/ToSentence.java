package lucene.file.search.service;

/**
 * @author:caoguangshuo
 * @date:2019/5/3
 * @descripstion:
 **/
import java.util.ArrayList;
import java.util.List;

import com.aliasi.sentences.IndoEuropeanSentenceModel;
import com.aliasi.sentences.SentenceModel;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;


/**
 * @author:caoguangshuo
 * @date:2019/4/27
 * @descripstion:
 **/
public class ToSentence {
    static final TokenizerFactory TOKENIZER_FACTORY = IndoEuropeanTokenizerFactory.INSTANCE;
    static final SentenceModel SENTENCE_MODEL = new IndoEuropeanSentenceModel();

    //这个是引用句子识别的方法，找了好多资料，在一个用它做文本分析里的找到的↓
    //https://blog.csdn.net/textboy/article/details/45580009
    public static List<String> ArticleToSentences(String text) {
        List<String> result = new ArrayList<String>();
        List<String> tokenList = new ArrayList<String>();
        List<String> whiteList = new ArrayList<String>();
        Tokenizer tokenizer = TOKENIZER_FACTORY.tokenizer(text.toCharArray(),0, text.length());
        tokenizer.tokenize(tokenList, whiteList);
        String[] tokens = new String[tokenList.size()];
        String[] whites = new String[whiteList.size()];
        tokenList.toArray(tokens);
        whiteList.toArray(whites);
        int[] sentenceBoundaries = SENTENCE_MODEL.boundaryIndices(tokens,whites);
        int sentStartTok = 0;
        int sentEndTok = 0;

        for (int i = 0; i < sentenceBoundaries.length; ++i) {
            System.out.println("Sentense " + (i + 1) + ", sentense's length(from 0):" + (sentenceBoundaries[i]));
            StringBuilder sb = new StringBuilder();
            sentEndTok = sentenceBoundaries[i];
            for (int j = sentStartTok; j <= sentEndTok; j++) {
                sb.append(tokens[j]).append(whites[j + 1]);
            }
            sentStartTok = sentEndTok + 1;
            result.add(sb.toString());
        }
        return result;
    }
    //替换中文标点符号，用于检测是否识别中文分句
    public String splitfuhao(String str) {
        String[] ChineseInterpunction = {"“", "”", "‘", "’", "。", "，", "；", "：", "？", "！", "……", "—", "～", "（", "）", "《", "》"};
        String[] EnglishInterpunction = {"\"", "\"", "'", "'", ".", ",", ";", ":", "?", "!", "…", "-", "~", "(", ")", "<", ">"};
        for (int j = 0; j < ChineseInterpunction.length; j++) {
            //alert("txt.replace("+ChineseInterpunction[j]+", "+EnglishInterpunction[j]+")");
            //String reg=str.matches(ChineseInterpunction[j],"g");
            str = str.replace(ChineseInterpunction[j], EnglishInterpunction[j] + " ");
        }
        return str;
    }

}
