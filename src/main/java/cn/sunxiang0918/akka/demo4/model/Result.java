package cn.sunxiang0918.akka.demo4.model;

import java.io.Serializable;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 10:33
 */
public class Result implements Serializable {

    private String word;            //单词
    private String name;        //数量

    public Result(String word, String name) {
        this.name = name;
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Result{" +
                "word='" + word + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
