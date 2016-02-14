package cn.sunxiang0918.akka.demo7;

import java.util.concurrent.TimeUnit;

import akka.actor.ActorRef;
import akka.actor.ReceiveTimeout;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.FromConfig;
import scala.concurrent.duration.Duration;

//#frontend
public class FactorialFrontend extends UntypedActor {
    final int upToN;        //计算到多少
    final boolean repeat;       //是否重复计算

    LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    /*获取到Backend的Router*/
    ActorRef backend = getContext().actorOf(FromConfig.getInstance().props(),
            "factorialBackendRouter");

    public FactorialFrontend(int upToN, boolean repeat) {
        this.upToN = upToN;
        this.repeat = repeat;
    }

    @Override
    public void preStart() {
        //因为是在Start前就发送消息,所以必定超时.
        sendJobs();
        getContext().setReceiveTimeout(Duration.create(10, TimeUnit.SECONDS));
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof FactorialResult) {
            FactorialResult result = (FactorialResult) message;
            if (result.n == upToN) {
                System.out.println("计算的结果:" + result);
                if (repeat)
                    sendJobs();
                else
                    getContext().stop(getSelf());
            }

        } else if (message instanceof ReceiveTimeout) {
            log.info("Timeout");
            sendJobs();

        } else {
            unhandled(message);
        }
    }

    void sendJobs() {
        log.info("Starting batch of factorials up to [{}]", upToN);
        for (int n = 1; n <= upToN; n++) {
            backend.tell(n, getSelf());
        }
    }

}

//#frontend

