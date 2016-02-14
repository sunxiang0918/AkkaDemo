package cn.sunxiang0918.akka.demo5;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.sunxiang0918.akka.demo5.actor.WriterActor;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.duration.Duration;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/6 21:39
 */
public class AkkaMain5 {

    public static void main(String[] args) throws Exception {
        
        final ActorSystem system = ActorSystem.create("demo5", ConfigFactory.load("demo5")
                .getConfig("demo5"));

//        // 创建一个到greeter Actor的管道
//        final ActorRef controlActor = system.actorOf(Props.create(ControlActor.class), "control");
//
//        controlActor.tell(new StartCommand(100),ActorRef.noSender());

        ActorRef actorRef = system.actorOf(Props.create(WriterActor.class));

        system.scheduler().scheduleOnce(Duration.create(5, TimeUnit.SECONDS),actorRef,"1111",system.dispatcher(),ActorRef.noSender());

//        Cancellable cancellable = system.scheduler().schedule(Duration.Zero(),Duration.create(1, TimeUnit.SECONDS),actorRef,"1111",system.dispatcher(),ActorRef.noSender());
        
        Thread.sleep(10000);
//        cancellable.cancel();
        //system.shutdown();
    }
}
