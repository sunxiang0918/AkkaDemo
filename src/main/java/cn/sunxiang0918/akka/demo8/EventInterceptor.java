package cn.sunxiang0918.akka.demo8;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import akka.actor.Terminated;
import akka.cluster.ClusterEvent;
import akka.cluster.Member;
import akka.cluster.MemberStatus;

public class EventInterceptor extends ClusterRoledWorker {

    private AtomicInteger interceptedRecords = new AtomicInteger(0);

    /*IP地址的正则表达式*/
    private Pattern IP_PATTERN = Pattern.compile("[^\\s]+\\s+\\[([^\\]]+)\\].+\"(\\d+\\.\\d+\\.\\d+\\.\\d+)");

    /*黑名单*/
    private List<String> blackIpList = Arrays.asList("5.9.116.101", "103.42.176.138", "123.182.148.65", "5.45.64.205",
            "27.159.226.192", "76.164.228.218", "77.79.178.186", "104.200.31.117",
            "104.200.31.32", "104.200.31.238", "123.182.129.108", "220.161.98.39",
            "59.58.152.90", "117.26.221.236", "59.58.150.110", "123.180.229.156",
            "59.60.123.239", "117.26.222.6", "117.26.220.88", "59.60.124.227",
            "142.54.161.50", "59.58.148.52", "59.58.150.85", "202.105.90.142");
    
    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof ClusterEvent.MemberUp){
            ClusterEvent.MemberUp member = (ClusterEvent.MemberUp) message;
            log.info("Member is Up: {}", member.member().address());
            
            register(member.member(), getCollectorPath(member.member()));
        }else if (message instanceof ClusterEvent.CurrentClusterState) {

            ClusterEvent.CurrentClusterState state = (ClusterEvent.CurrentClusterState) message;
            Iterable<Member> members = state.getMembers();
            
            // 如果加入Akka集群的成员节点是Up状态，并且是collector角色，则调用register向collector进行注册
            members.forEach(o -> {
                if (o.status() == MemberStatus.up()){
                    register(o,getCollectorPath(o));
                }
            });
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
        }else if (message instanceof EventMessages.NginxRecord) {
            EventMessages.NginxRecord nginxRecord = (EventMessages.NginxRecord) message;

            CheckRecord checkRecord = checkRecord(nginxRecord.getEventCode(), nginxRecord.getLine());
            
            if (!checkRecord.isIpInBlackList){
                int records = interceptedRecords.incrementAndGet();
                
                if (workers.size()>0){
                    int processorIndex = (records<0?0:records) % workers.size();
                    workers.get(processorIndex).tell(new EventMessages.FilteredRecord(nginxRecord.getSourceHost(),nginxRecord.getLine(), nginxRecord.getEventCode() , checkRecord.data.get("eventdate"), checkRecord.data.get("realip")),getSelf());
                    log.info("Details: processorIndex=" + processorIndex + ", processors=" + workers.size());
                }
                log.info("Intercepted data: data=" + checkRecord.data);
            }else {
                log.info("Discarded: " + nginxRecord.getLine());
            }
        }
    }

    /**
     * 检查和解析每一行日志的记录
     * @param eventCode
     * @param line
     * @return
     */
    private CheckRecord checkRecord(String eventCode,String line){
        
        Map<String,String> data = new HashMap<>();
        boolean isIpInBlackList = false;

        Matcher matcher = IP_PATTERN.matcher(line);

        while (matcher.find()) {
            String rawDt = matcher.group(1);
            String realIp = matcher.group(2);

            data.put("eventdate", rawDt);
            data.put("realip", realIp);
            data.put("eventcode", eventCode);
            
            isIpInBlackList = blackIpList.contains(realIp);
        }
        
        return new CheckRecord(isIpInBlackList,data);
    }

    /**
     * 获取Collector的路径
     * @param member
     * @return
     */
    private String getCollectorPath(Member member){
        return member.address()+"/user/collectingActor";
    }
    
    private class CheckRecord {
        private boolean isIpInBlackList;
        
        private Map<String,String> data;

        public CheckRecord(boolean isIpInBlackList, Map<String, String> data) {
            this.isIpInBlackList = isIpInBlackList;
            this.data = data;
        }
    }
}
