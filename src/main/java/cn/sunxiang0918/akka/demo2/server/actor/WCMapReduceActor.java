package cn.sunxiang0918.akka.demo2.server.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 10:30
 */
public class WCMapReduceActor extends UntypedActor{

    private ActorRef mapRouter;
    private ActorRef aggregateActor;

    @Override
    public void preStart() throws Exception {
        System.out.println("启动WCMapReduceActor:"+Thread.currentThread().getName());
    }
    
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String) {
            /*如果接收到的是显示结果的请求,那么就调用reduce的Actor*/
            if (((String) message).compareTo("DISPLAY_LIST") == 0) {
                System.out.println("Got Display Message");
                aggregateActor.tell(message, getSender());
            }if (message.equals("EOF")){
                //表示发送完毕
                aggregateActor.tell(true, getSender());
            }else {
                /*否则给map的Actor进行计算*/
                mapRouter.tell(message,null);
            }
        }
    }

    public WCMapReduceActor(ActorRef inAggregateActor, ActorRef inMapRouter) {
        mapRouter = inMapRouter;
        aggregateActor = inAggregateActor;
    }
}
