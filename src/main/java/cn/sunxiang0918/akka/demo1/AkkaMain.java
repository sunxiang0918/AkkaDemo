package cn.sunxiang0918.akka.demo1;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Inbox;
import akka.actor.Props;
import cn.sunxiang0918.akka.demo1.actor.GreetPrinter;
import cn.sunxiang0918.akka.demo1.actor.Greeter;
import cn.sunxiang0918.akka.demo1.model.Greet;
import cn.sunxiang0918.akka.demo1.model.Greeting;
import cn.sunxiang0918.akka.demo1.model.WhoToGreet;
import scala.concurrent.duration.Duration;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/6 21:39
 */
public class AkkaMain {

    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create("helloakka");

        // 创建一个到greeter Actor的管道
        final ActorRef greeter = system.actorOf(Props.create(Greeter.class), "greeter");

        System.out.println(greeter.path());
        
        final ActorSelection selection = system.actorSelection("akka://helloakka/user/greeter");
        selection.tell(new WhoToGreet("akka"), ActorRef.noSender());

        // 创建邮箱
        final Inbox inbox = Inbox.create(system);

        // 先发第一个消息,消息类型为WhoToGreet
//        greeter.tell(new WhoToGreet("akka"), ActorRef.noSender());

        // 真正的发送消息,消息体为Greet
        inbox.send(greeter, new Greet());

        // 等待5秒尝试接收Greeter返回的消息
        Greeting greeting1 = (Greeting) inbox.receive(Duration.create(5, TimeUnit.SECONDS));
        System.out.println("Greeting: " + greeting1.message);

        // 发送第三个消息,修改名字
        greeter.tell(new WhoToGreet("typesafe"), ActorRef.noSender());
        // 发送第四个消息
        inbox.send(greeter, new Greet());
        
        // 等待5秒尝试接收Greeter返回的消息
        Greeting greeting2 = (Greeting) inbox.receive(Duration.create(5, TimeUnit.SECONDS));
        System.out.println("Greeting: " + greeting2.message);

        // 新创建一个Actor的管道
        ActorRef greetPrinter = system.actorOf(Props.create(GreetPrinter.class));
        
        //使用schedule 每一秒发送一个Greet消息给 greeterActor,然后把greeterActor的消息返回给greetPrinterActor 
        system.scheduler().schedule(Duration.Zero(), Duration.create(1, TimeUnit.SECONDS), greeter, new Greet(), system.dispatcher(), greetPrinter);
        //system.shutdown();
    }
}
