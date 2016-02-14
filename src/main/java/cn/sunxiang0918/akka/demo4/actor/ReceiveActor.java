package cn.sunxiang0918.akka.demo4.actor;

import java.util.UUID;

import akka.actor.UntypedActor;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 20:27
 */
public class ReceiveActor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        System.out.println("接收到消息:"+message);
        String content = Thread.currentThread().getName()+":"+ UUID.randomUUID().toString();
        //返回结果
        getSender().tell(content,null);
    }
}
