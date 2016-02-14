package cn.sunxiang0918.akka.demo3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;
import cn.sunxiang0918.akka.demo3.actor.JobControllerActor;
import cn.sunxiang0918.akka.demo3.actor.WorkerActor;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/6 22:37
 */
public class AkkaMain3 {

    static final int no_of_msgs = 10000;

    final int no_of_workers = 10;

    final ActorRef router;

    public AkkaMain3() {
        final int no_of_workers = 10000;

        ActorSystem system = ActorSystem.create("LoadGeneratorApp");

        final ActorRef appManager = system.actorOf(Props.create(JobControllerActor.class, no_of_msgs), "jobController");//启动jobController

        router = system.actorOf(Props.create(WorkerActor.class, appManager).withRouter(new RoundRobinPool(no_of_workers)));   //启动带RoundRobinRouter的workerActor

    }

    public static void main(String[] args) throws Exception {
        new AkkaMain3().generateLoad();
    }

    private void generateLoad() {//开始产生1000万消息
        for (int i = no_of_msgs; i >= 0; i--) {
            router.tell("Job Id " + i + "# send", ActorRef.noSender());
        }
        System.out.println("All jobs sent successfully");
    }
}
