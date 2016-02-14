package cn.sunxiang0918.akka.demo4;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.Futures;
import akka.dispatch.Mapper;
import akka.util.Timeout;
import cn.sunxiang0918.akka.demo4.actor.ReceiveActor;
import cn.sunxiang0918.akka.demo4.actor.ResultActor;
import cn.sunxiang0918.akka.demo4.model.Result;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

/**
 * @author SUN
 * @version 1.0
 * @Date 16/1/7 20:26
 */
public class AkkaMain4 {

    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create("demo4");

        //超时时间
        final Timeout t = new Timeout(Duration.create(5, TimeUnit.SECONDS));

        //异步反馈futures
        final ArrayList<Future<Object>> futures = new ArrayList<>();

        futures.add(ask(system.actorOf(Props.create(ReceiveActor.class)), "request", 1000)); // using 1000ms timeout
        futures.add(ask(system.actorOf(Props.create(ReceiveActor.class)), "another request", t)); // using timeout from

        /*定义结果聚合管道*/
        final Future<Iterable<Object>> aggregate = Futures.sequence(futures,
                system.dispatcher());

        /*对聚合的结果做map操作.*/
        final Future<Result> transformed = aggregate.map(
                new Mapper<Iterable<Object>, Result>() {
                    public Result apply(Iterable<Object> coll) {
                        /*这里面就是所有的ask的结果.这里明确的晓得是两次,所以就调用两次next*/
                        final Iterator<Object> it = coll.iterator();
                        final String x = (String) it.next();
                        final String s = (String) it.next();
                        return new Result(x, s);
                    }
                }, system.dispatcher());

        pipe(transformed, system.dispatcher()).to(system.actorOf(Props.create(ResultActor.class)));
    }
}
