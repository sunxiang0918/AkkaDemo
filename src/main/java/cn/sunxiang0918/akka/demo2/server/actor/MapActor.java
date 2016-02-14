package cn.sunxiang0918.akka.demo2.server.actor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import cn.sunxiang0918.akka.demo2.server.model.Result;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 10:33
 */
public class MapActor extends UntypedActor {

    //停用词
    String[] STOP_WORDS = {"a", "about", "above", "above", "across", "after",
            "afterwards", "again", "against", "all", "almost", "alone",
            "along", "already", "also", "although", "always", "am", "among",
            "amongst", "amoungst", "amount", "an", "and", "another", "any",
            "anyhow", "anyone", "anything", "anyway", "anywhere", "are",
            "around", "as", "at", "back", "be", "became", "because", "become",
            "becomes", "becoming", "been", "before", "beforehand", "behind",
            "being", "below", "beside", "besides", "between", "beyond", "bill",
            "both", "bottom", "but", "by", "call", "can", "cannot", "cant",
            "co", "con", "could", "couldnt", "cry", "de", "describe", "detail",
            "do", "done", "down", "due", "during", "each", "eg", "eight",
            "either", "eleven", "else", "elsewhere", "empty", "enough", "etc",
            "even", "ever", "every", "everyone", "everything", "everywhere",
            "except", "few", "fifteen", "fify", "fill", "find", "fire",
            "first", "five", "for", "former", "formerly", "forty", "found",
            "four", "from", "front", "full", "further", "get", "give", "go",
            "had", "has", "hasnt", "have", "he", "hence", "her", "here",
            "hereafter", "hereby", "herein", "hereupon", "hers", "herself",
            "him", "himself", "his", "how", "however", "hundred", "ie", "if",
            "in", "inc", "indeed", "interest", "into", "is", "it", "its",
            "itself", "keep", "last", "latter", "latterly", "least", "less",
            "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill",
            "mine", "more", "moreover", "most", "mostly", "move", "much",
            "must", "my", "myself", "name", "namely", "neither", "never",
            "nevertheless", "next", "nine", "no", "nobody", "none", "noone",
            "nor", "not", "nothing", "now", "nowhere", "of", "off", "often",
            "on", "once", "one", "only", "onto", "or", "other", "others",
            "otherwise", "our", "ours", "ourselves", "out", "over", "own",
            "part", "per", "perhaps", "please", "put", "rather", "re", "same",
            "see", "seem", "seemed", "seeming", "seems", "serious", "several",
            "she", "should", "show", "side", "since", "sincere", "six",
            "sixty", "so", "some", "somehow", "someone", "something",
            "sometime", "sometimes", "somewhere", "still", "such", "system",
            "take", "ten", "than", "that", "the", "their", "them",
            "themselves", "then", "thence", "there", "thereafter", "thereby",
            "therefore", "therein", "thereupon", "these", "they", "thickv",
            "thin", "third", "this", "those", "though", "three", "through",
            "throughout", "thru", "thus", "to", "together", "too", "top",
            "toward", "towards", "twelve", "twenty", "two", "un", "under",
            "until", "up", "upon", "us", "very", "via", "was", "we", "well",
            "were", "what", "whatever", "when", "whence", "whenever", "where",
            "whereafter", "whereas", "whereby", "wherein", "whereupon",
            "wherever", "whether", "which", "while", "whither", "who",
            "whoever", "whole", "whom", "whose", "why", "will", "with",
            "within", "without", "would", "yet", "you", "your", "yours",
            "yourself", "yourselves", "the"};

    List<String> STOP_WORDS_LIST = Arrays.asList(STOP_WORDS);

    /*reduce聚合的Actor*/
    private ActorRef actor = null;

    public MapActor(ActorRef inReduceActor) {
        actor = inReduceActor;
    }

    @Override
    public void preStart() throws Exception {
        System.out.println("启动MapActor:"+Thread.currentThread().getName());
    }
    
    /**
     * 用于分词 计算单词的数量的
     * @param line
     * @return
     */
    private List<Result> evaluateExpression(String line) {
        List<Result> list = new ArrayList<>();
        /*字符串分词器*/
        StringTokenizer parser = new StringTokenizer(line);
        while (parser.hasMoreTokens()) {
            /*如果是,那么就判断是否是字母.然后把结果记录下来*/
            String word = parser.nextToken().toLowerCase();
            if (isAlpha(word) && !STOP_WORDS_LIST.contains(word)) {
                list.add(new Result(word, 1));
            }
        }
        return list;
    }

    /**
     * 判断是否是字母
     * @param s
     * @return
     */
    private boolean isAlpha(String s) {
        s = s.toUpperCase();
        for (int i = 0; i < s.length(); i++) {
            int c = (int) s.charAt(i);
            if (c < 65 || c > 90)
                return false;
        }
        return true;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            String work = (String) message;
            
            if (work.equals("EOF")){
                /*表示已经结束了*/
                actor.tell(true,null);
                return;
            }
            
            // 计算这一行的单词情况
            List<Result> list = evaluateExpression(work);

            // 把这一行的单词情况发送给汇总的ReduceActor
            actor.tell(list, null);
        } else
            throw new IllegalArgumentException("Unknown message [" + message + "]");
    }
}
