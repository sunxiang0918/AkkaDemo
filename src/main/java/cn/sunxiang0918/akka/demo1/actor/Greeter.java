package cn.sunxiang0918.akka.demo1.actor;

import akka.actor.UntypedActor;
import cn.sunxiang0918.akka.demo1.model.Greet;
import cn.sunxiang0918.akka.demo1.model.Greeting;
import cn.sunxiang0918.akka.demo1.model.WhoToGreet;

/**
 * 打招呼的Actor
 * @author SUN
 * @version 1.0
 * @Date 16/1/6 21:40
 */
public class Greeter extends UntypedActor{

    String greeting = "";
    
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof WhoToGreet)
            greeting = "hello, " + ((WhoToGreet) message).who;
        else if (message instanceof Greet)
            // 发送招呼消息给发送消息给这个Actor的Actor
            getSender().tell(new Greeting(greeting), getSelf());

        else unhandled(message);
    }
}
