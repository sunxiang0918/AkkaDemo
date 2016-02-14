package cn.sunxiang0918.akka.demo6;

import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class SimpleClusterApp {

    public static void main(String[] args) {
        if (args.length == 0)
            /*启动三个节点*/
            startup(new String[]{"2551", "2552", "0"});
        else
            startup(args);
    }

    public static void startup(String[] ports) {
        for (String port : ports) {
            // 重写配置中的远程端口
            Config config = ConfigFactory.parseString(
                    "akka.remote.netty.tcp.port=" + port).withFallback(
                    ConfigFactory.load("demo6"));

            // 创建ActorSystem,名称需要和conf配置文件中的相同
            ActorSystem system = ActorSystem.create("ClusterSystem", config);

            // 创建集群中的Actor,并监听事件
            system.actorOf(Props.create(SimpleClusterListener.class),
                    "clusterListener");

        }
    }
}
