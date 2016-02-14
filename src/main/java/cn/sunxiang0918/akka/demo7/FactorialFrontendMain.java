package cn.sunxiang0918.akka.demo7;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.cluster.Cluster;

public class FactorialFrontendMain {

  public static void main(String[] args) {
    final int upToN = 10;

    final Config config = ConfigFactory.parseString(
        "akka.cluster.roles = [frontend]").withFallback(
        ConfigFactory.load("demo7"));

    final ActorSystem system = ActorSystem.create("ClusterSystem", config);
    system.log().info(
        "Factorials will start when 2 backend members in the cluster.");
    //#registerOnUp
    Cluster.get(system).registerOnMemberUp((Runnable) () -> system.actorOf(Props.create(FactorialFrontend.class, upToN, false),
        "factorialFrontend"));
    //#registerOnUp
  }

}
