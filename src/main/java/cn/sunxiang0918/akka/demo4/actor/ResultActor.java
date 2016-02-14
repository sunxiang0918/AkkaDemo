package cn.sunxiang0918.akka.demo4.actor;

import akka.actor.UntypedActor;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 20:34
 */
public class ResultActor extends UntypedActor{

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("接收到消息:"+message);
    }
}
