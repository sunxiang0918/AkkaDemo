package cn.sunxiang0918.akka.demo2.client.actor;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 10:20
 */
public class ClientActor extends UntypedActor {

    private ActorRef remoteServer = null;
    private long start;

    /**
     * @param inRemoteServer
     */
    public ClientActor(ActorRef inRemoteServer) {
        remoteServer = inRemoteServer;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        /*如果接收到的任务是String的,那么就直接发送给remoteServer这个Actor*/
        if (message instanceof String) {
            String msg = (String) message;
            if (message.equals("EOF")){
                //这个的Sender设置为自己是为了接收聚合完成的消息
                remoteServer.tell(msg, getSelf());
            }else{
                remoteServer.tell(msg, null);
            }
        }else if (message instanceof Boolean) {
            System.out.println("聚合完成");
            //聚合完成后发送显示结果的消息
            remoteServer.tell("DISPLAY_LIST",null);

            //执行完毕,关机
            getContext().stop(self());
        }
    }

    @Override
    public void preStart() {
        /*记录开始时间*/
        start = System.currentTimeMillis();
    }

    @Override
    public void postStop() {
        /*计算用时*/
        // tell the world that the calculation is complete
        long timeSpent = (System.currentTimeMillis() - start);
        System.out
                .println(String
                        .format("\n\tClientActor estimate: \t\t\n\tCalculation time: \t%s MS",
                                timeSpent));
    }
}
