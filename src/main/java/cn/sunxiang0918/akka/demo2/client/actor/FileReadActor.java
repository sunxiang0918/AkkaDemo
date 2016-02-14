package cn.sunxiang0918.akka.demo2.client.actor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import akka.actor.UntypedActor;

/**
 * 从文件里面获取出行数
 *
 * @author SUN
 * @version 1.0
 * @Date 16/1/6 22:25
 */
public class FileReadActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            /*如果消息是String类型的*/
            String fileName = (String) message;
            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(Thread.currentThread().getContextClassLoader().getResource(fileName).openStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    /*遍历,一行一个消息反馈给消息发送方*/
                    getSender().tell(line,null);
                }
                System.out.println("All lines send !");
                /*发送一个结束标识*/
                getSender().tell(String.valueOf("EOF"),null);
            } catch (IOException x) {
                System.err.format("IOException: %s%n", x);
            }
        } else {
            throw new IllegalArgumentException("Unknown message [" + message + "]");
        }
    }
}
