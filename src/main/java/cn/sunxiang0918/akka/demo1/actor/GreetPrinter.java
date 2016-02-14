package cn.sunxiang0918.akka.demo1.actor;

import akka.actor.UntypedActor;
import cn.sunxiang0918.akka.demo1.model.Greeting;

/**
 * 打印招呼
 * @author SUN
 * @version 1.0
 * @Date 16/1/6 21:45
 */
public class GreetPrinter extends UntypedActor{

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof Greeting)
            System.out.println(((Greeting) message).message);
    }
}
