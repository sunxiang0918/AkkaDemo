package cn.sunxiang0918.akka.demo2.client;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import cn.sunxiang0918.akka.demo2.client.actor.ClientActor;
import cn.sunxiang0918.akka.demo2.client.actor.FileReadActor;
import com.typesafe.config.ConfigFactory;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 10:19
 */
public class ClientMain {

    public static void main(String[] args) throws Exception {

        //文件名
        final String fileName = "Othello.txt";

        /*根据配置,找到System*/
        ActorSystem system = ActorSystem.create("ClientApplication", ConfigFactory.load("client").getConfig("WCMapReduceClientApp"));

        /*实例化远程Actor*/
        final ActorRef remoteActor = system.actorFor("akka.tcp://WCMapReduceApp@127.0.0.1:2552/user/WCMapReduceActor");

        /*实例化Actor的管道*/
        final ActorRef fileReadActor = system.actorOf(Props.create(FileReadActor.class));
        
        /*实例化Client的Actor管道*/
        final ActorRef clientActor = system.actorOf(Props.create(ClientActor.class,remoteActor));
        
        /*发送文件名给fileReadActor.设置sender或者说回调的Actor为clientActor*/
        fileReadActor.tell(fileName,clientActor);

    }
}
