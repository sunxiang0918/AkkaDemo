package cn.sunxiang0918.akka.demo2.server;

import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.dispatch.PriorityGenerator;
import akka.dispatch.UnboundedPriorityMailbox;
import com.typesafe.config.Config;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 13:32
 */
public class MyPriorityMailBox extends UnboundedPriorityMailbox {

    /**
     * 创建一个自定义优先级的无边界的邮箱. 用来规定命令的优先级. 这个就保证了DISPLAY_LIST 这个事件是最后再来处理.
     */
    public MyPriorityMailBox(ActorSystem.Settings settings, Config config) {

        // Creating a new PriorityGenerator,
        super(new PriorityGenerator() {
            @Override
            public int gen(Object message) {
                if (message.equals("DISPLAY_LIST"))
                    return 2; // 'DisplayList messages should be treated
                    // last if possible
                else if (message.equals(PoisonPill.getInstance()))
                    return 3; // PoisonPill when no other left
                else
                    return 0; // By default they go with high priority
            }
        });
    }

}
