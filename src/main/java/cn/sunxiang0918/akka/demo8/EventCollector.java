package cn.sunxiang0918.akka.demo8;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import akka.actor.Terminated;
import akka.cluster.ClusterEvent;

public class EventCollector extends ClusterRoledWorker {

    private AtomicInteger recordCounter = new AtomicInteger(0);


    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ClusterEvent.MemberUp) {
            ClusterEvent.MemberUp member = (ClusterEvent.MemberUp) message;
            log.info("Member is Up: {}", member.member().address());
        } else if (message instanceof ClusterEvent.UnreachableMember) {
            ClusterEvent.UnreachableMember mUnreachable = (ClusterEvent.UnreachableMember) message;
            log.info("Member detected as unreachable: {}", mUnreachable.member());
        } else if (message instanceof ClusterEvent.MemberRemoved) {
            ClusterEvent.MemberRemoved mRemoved = (ClusterEvent.MemberRemoved) message;
            log.info("Member is Removed: {}", mRemoved.member());
        } else if (message instanceof ClusterEvent.MemberEvent) {
            // ignore
            log.info("Member Event: {}", ((ClusterEvent.MemberEvent) message).member());
        } else if (message instanceof EventMessages.Registration) {
            // watch发送注册消息的interceptor，如果对应的Actor终止了，会发送一个Terminated消息
            getContext().watch(getSender());
            workers.add(getSender());
            log.info("Interceptor registered: " + getSender());
            log.info("Registered interceptors: " + workers.size());
        } else if (message instanceof Terminated) {
            // interceptor终止，更新缓存的ActorRef
            Terminated terminated = (Terminated) message;
            workers.remove(terminated.actor());
        } else if (message instanceof EventMessages.RawNginxRecord) {
            EventMessages.RawNginxRecord rawNginxRecord = (EventMessages.RawNginxRecord) message;
            String line = rawNginxRecord.getLine();
            String sourceHost = rawNginxRecord.getSourceHost();
            String eventCode = findEventCode(line);

            // 构造NginxRecord消息，发送到下游interceptor
            log.info("Raw message: eventCode=" + eventCode + ", sourceHost=" + sourceHost + ", line=" + line);
            int counter = recordCounter.incrementAndGet();
            if (workers.size() > 0) {
                // 模拟Roudrobin方式，将日志记录消息发送给下游一组interceptor中的一个
                int interceptorIndex = (counter < 0 ? 0 : counter) % workers.size();
                workers.get(interceptorIndex).tell(new EventMessages.NginxRecord(sourceHost, line, eventCode), getSelf());
                log.info("Details: interceptorIndex=" + interceptorIndex + ", interceptors=" + workers.size());
            }
        }
    }

    private String findEventCode(String line) {
        Pattern pattern = Pattern.compile("eventcode=(\\d+)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
