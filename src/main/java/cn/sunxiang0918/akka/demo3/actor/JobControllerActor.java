package cn.sunxiang0918.akka.demo3.actor;

import akka.actor.UntypedActor;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/6 22:33
 */
public class JobControllerActor extends UntypedActor{

    int count = 0;
    long startedTime = System.currentTimeMillis();
    int no_of_msgs = 0;

    public JobControllerActor(int no_of_msgs) {
        this.no_of_msgs = no_of_msgs;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof String){
            if (message.equals("DONE")){
                count++;
                if (count == no_of_msgs) {//计数到达1000万结束，统计时间
                    long now = System.currentTimeMillis();
                    System.out.println("All messages processed in "
                            + (now - startedTime)+" millionseconds");

                    System.out.println("Total Number of messages processed "
                            + count);
                    getContext().system().shutdown();
                }
            }
        }
    }
}
