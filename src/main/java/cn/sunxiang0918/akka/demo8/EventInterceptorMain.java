package cn.sunxiang0918.akka.demo8;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class EventInterceptorMain {

    public static void main(String[] args) throws Exception {
        
        final String port = args.length > 0 ? args[0] : "0";
        
        /*修改配置文件中的端口和角色*/
        final Config config = ConfigFactory.parseString("akka.remote.netty.tcp.port=" + port).
                withFallback(ConfigFactory.parseString("akka.cluster.roles = [interceptor]")).
                withFallback(ConfigFactory.load("demo8"));

        final ActorSystem system = ActorSystem.create("event-cluster-system", config);

        /*实例化EventInterceptor Actor*/
        ActorRef interceptingActor = system.actorOf(Props.create(EventInterceptor.class), "interceptingActor");

        system.log().info("Processing Actor: " + interceptingActor);
    }
}
